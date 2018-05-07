package com.codeiscoffee.processing.controller;

import com.codeiscoffee.processing.data.Sale;
import com.codeiscoffee.processing.exceptions.MessageLimitException;
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
@Slf4j
public class SalesController {

    private SalesService salesService;
    private ReportingService reportingService;

    @RequestMapping(value = "/sale", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Add one sale for the product type specified at the value provided.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sale of product has been registered")
    })
    public ResponseEntity<String> registerSale(@RequestParam(value = "productType") String productType, @RequestParam(value = "value") Double value) {
        try{
            if(salesService.getSuccessfulMessages() < 50){
                return registerSingleSale(productType, value);
            }
            throw new MessageLimitException(salesService.getSuccessfulMessages());
        }
        catch (MessageLimitException e){
            HttpHeaders headers = new HttpHeaders();
            HttpStatus status = HttpStatus.FORBIDDEN;
            String body = generateErrorResponse(productType, value, e);
            return new ResponseEntity<>(body, headers, status);
        }
    }

    private ResponseEntity<String> registerSingleSale(String productType, Double value) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;
        String body;
        try {
            Sale sale = salesService.registerSale(productType, value);
            if(salesService.getSuccessfulMessages() % 10 == 0){
                reportingService.reportSales();
            }
            body = generateSuccessfulSaleResponse(sale);
            status = HttpStatus.CREATED;
        } catch (IllegalArgumentException e) {
            body = generateErrorResponse(productType, value, e);
            status = HttpStatus.BAD_REQUEST;
            log.error("Error with parameters for sale of " + productType + " at price " + value, e);
        } catch (Exception e) {
            body = generateErrorResponse(productType, value, e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Internal error occurred when registering sale of " + productType + " at price " + value, e);
        }
        return new ResponseEntity<>(body, headers, status);
    }

    private String generateSuccessfulSaleResponse(Sale sale) {
        String body;
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.add("sale", gson.toJsonTree(sale));
        json.addProperty("successfullyProcessedMessages", salesService.getSuccessfulMessages());
        body = json.toString();
        return body;
    }

    private String generateErrorResponse(String productType, Double value, Exception e) {
        String body;
        JsonObject json = new JsonObject();
        json.addProperty("errorMessage", e.getMessage());
        json.addProperty("productType", productType);
        json.addProperty("value", value);
        json.addProperty("successfullyProcessedMessages", salesService.getSuccessfulMessages());
        body = json.toString();
        return body;
    }
}
