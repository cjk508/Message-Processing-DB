package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Sale;
import com.codeiscoffee.processing.data.Sales;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class SalesService {

    @Getter
    private Sales sales = new Sales();
    @Getter
    private int successfulMessages = 0;

    public Sale registerSale(String productType, Double value, int occurrences) {
        validateProductType(productType);
        validateValue(value);
        Sale sale = new Sale(productType,value,occurrences);
        sales.addSale(sale);
        successfulMessages++;
        return sale;
    }

    private void validateValue(Double value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException(value + " is less than 0. All values must be positive");
        }
    }

    private void validateProductType(String productType) {
        if (StringUtils.isEmpty(productType)) {
            throw new IllegalArgumentException("ProductType cannot be blank");
        }

    }
}
