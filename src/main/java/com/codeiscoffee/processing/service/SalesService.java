package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.data.sales.Sales;
import com.codeiscoffee.processing.validation.ProductValueValidation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesService implements ProductValueValidation {

    @Getter
    private Sales sales = new Sales();

    public Sale registerSale(String productType, Double value, int units) {
        validateProductType(productType);
        validateValue(value);
        Sale sale = new Sale(productType, units, value);
        sales.addSale(sale);
        return sale;
    }
}
