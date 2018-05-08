package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.data.sales.Sales;
import com.codeiscoffee.processing.util.ProductValueValidator;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SalesService  {

    @Getter
    private Sales sales = new Sales();

    public Sale registerSale(String productType, Double value, int units) {
        productType = ProductValueValidator.validateAndTrimProductType(productType);
        ProductValueValidator.validateValue(value);
        validateUnits(units);
        Sale sale = new Sale(productType, units, value);
        sales.addSale(sale);
        return sale;
    }

    private void validateUnits(int units) {
        if (units <= 0) {
            throw new IllegalArgumentException(units + " is less than or equal to 0. All units must be positive");
        }
    }
}
