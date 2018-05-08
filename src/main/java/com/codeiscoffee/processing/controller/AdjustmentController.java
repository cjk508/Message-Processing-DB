package com.codeiscoffee.processing.controller;

import com.codeiscoffee.processing.data.Operator;
import com.codeiscoffee.processing.data.adjustment.Adjustment;
import com.codeiscoffee.processing.exceptions.MessageLimitException;
import com.codeiscoffee.processing.service.AdjustmentService;
import com.codeiscoffee.processing.service.MessageCountService;
import com.codeiscoffee.processing.service.ReportingService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j(topic = "org.codeiscoffee")
public class AdjustmentController {

    private AdjustmentService adjService;
    private ReportingService reportingService;
    private MessageCountService messageCountService;

    @RequestMapping(value = "/adjustment", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Adjust historic sales for 1 product using a set of 3 operations (Add, Subtract or Multiply). If any operation causes " +
            "a historic sale to drop below zero then it will fail and the change will be reverted.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Adjustment has been created")
    })
    public ResponseEntity<String> registerAdjustment(@RequestParam(value = "productType") String productType, @RequestParam(value = "value") Double value, @RequestParam(value = "operator") Operator operator) {
        HttpStatus errorStatus;
        String errorBody;
        try {
            if (messageCountService.getSuccessfulMessages() < 50) {
                return processAdjustment(productType, value, operator);
            }
            throw new MessageLimitException(messageCountService.getSuccessfulMessages());
        } catch (MessageLimitException e) {
            errorStatus = HttpStatus.FORBIDDEN;
            errorBody = generateErrorBody(productType, value, operator, e);

        } catch (Exception e) {
            errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorBody = generateErrorBody(productType, value, operator, e);
            log.error("Error when adjusting sales of " + productType + " with adjustment " + operator.getSymbol() + value, e);
        }
        return new ResponseEntity<>(errorBody, new HttpHeaders(), errorStatus);
    }

    private ResponseEntity<String> processAdjustment(String productType, Double value, Operator operator) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;
        String body;
        try {
            Adjustment adjustment = adjService.processAdjustment(productType, value, operator);

            messageCountService.registerSuccessfulMessage();
            reportingService.printReportsWhenNecessary();

            body = generateSuccessBody(adjustment);
            status = HttpStatus.CREATED;

        } catch (IllegalArgumentException e) {
            body = generateErrorBody(productType, value, operator, e);
            status = HttpStatus.BAD_REQUEST;
            log.error("Error when adjusting sales of " + productType + " with adjustment " + operator.getSymbol() + value, e);
        }
        return new ResponseEntity<>(body, headers, status);
    }

    private String generateSuccessBody(Adjustment adjustment) {
        String body;
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.add("adjustment", gson.toJsonTree(adjustment));
        json.addProperty("successfullyProcessedMessages", messageCountService.getSuccessfulMessages());
        body = json.toString();
        return body;
    }

    private String generateErrorBody(String productType, Double value, Operator operator, Exception e) {
        String body;
        JsonObject json = new JsonObject();
        json.addProperty("errorMessage", e.getMessage());
        json.addProperty("productType", productType);
        json.addProperty("value", value);
        json.addProperty("operator", operator.toString());
        json.addProperty("successfullyProcessedMessages", messageCountService.getSuccessfulMessages());
        body = json.toString();
        return body;
    }
}
