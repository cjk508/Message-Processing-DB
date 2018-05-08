package com.codeiscoffee.processing.controller;

import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.exceptions.MessageLimitException;
import com.codeiscoffee.processing.service.MessageCountService;
import com.codeiscoffee.processing.service.ReportingService;
import com.codeiscoffee.processing.service.SalesService;
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
public class SalesController {

    private SalesService salesService;
    private ReportingService reportingService;
    private MessageCountService messageCountService;

    @RequestMapping(value = "/sale", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Add sale record for at least 1 unit for the product type specified at the value provided.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sale of product has been registered")
    })
    public ResponseEntity<String> registerSales(@RequestParam(value = "productType") String productType, @RequestParam(value = "value") Double value, @RequestParam(value = "units", required = false, defaultValue = "1") int units) {
        HttpStatus errorStatus;
        String errorBody;
        try {
            if (messageCountService.getSuccessfulMessages() < 50) {
                return processSales(productType, value, units);
            }
            throw new MessageLimitException(messageCountService.getSuccessfulMessages());
        } catch (MessageLimitException e) {
            errorStatus = HttpStatus.FORBIDDEN;
            errorBody = generateErrorBody(productType, value, units, e);

        } catch (Exception e) {
            errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorBody = generateErrorBody(productType, value, units, e);
            log.error("Internal error occurred when registering sale of " + productType + " at price " + value + " for " + units + " units", e);
        }
        return new ResponseEntity<>(errorBody, new HttpHeaders(), errorStatus);
    }

    private ResponseEntity<String> processSales(String productType, Double value, int occurrences) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;
        String body;
        try {

            Sale sale = salesService.registerSale(productType, value, occurrences);
            messageCountService.registerSuccessfulMessage();
            reportingService.printReportsWhenNecessary();

            body = generateSuccessBody(sale);
            status = HttpStatus.CREATED;

        } catch (IllegalArgumentException e) {
            body = generateErrorBody(productType, value, occurrences, e);
            status = HttpStatus.BAD_REQUEST;
            log.error("Error with parameters for sale of " + productType + " at price £" + value + " for " + occurrences + " occurrences", e);
        }
        return new ResponseEntity<>(body, headers, status);
    }

    private String generateSuccessBody(Sale sale) {
        String body;
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.add("sale", gson.toJsonTree(sale));
        json.addProperty("successfullyProcessedMessages", messageCountService.getSuccessfulMessages());
        body = json.toString();
        return body;
    }

    private String generateErrorBody(String productType, Double value, int occurrences, Exception e) {
        String body;
        JsonObject json = new JsonObject();
        json.addProperty("errorMessage", e.getMessage());
        json.addProperty("productType", productType);
        json.addProperty("value", value);
        json.addProperty("occurrences", occurrences);
        json.addProperty("successfullyProcessedMessages", messageCountService.getSuccessfulMessages());
        body = json.toString();
        return body;
    }
}
