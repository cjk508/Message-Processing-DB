package com.codeiscoffee.processing.util;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class ProductValueValidator {

    private ProductValueValidator() {
    }

    public static void validateValue(Double value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException(value + " is less than or equal to 0. All values must be positive");
        }
    }

    public static String validateAndTrimProductType(String productType) {
        if (StringUtils.isEmpty(productType)) {
            throw new IllegalArgumentException("ProductType cannot be blank");
        }
        return productType.trim();
    }

}
