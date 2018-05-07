package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.data.sales.Sales;
import com.codeiscoffee.processing.validation.ProductValueValidation;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SalesService implements ProductValueValidation {

    @Getter
    private Sales sales = new Sales();

    public Sale registerSale(String productType, Double value, int units) {
        productType = validateProductType(productType);
        validateValue(value);
        Sale sale = new Sale(productType, units, value);
        sales.addSale(sale);
        return sale;
    }
}
