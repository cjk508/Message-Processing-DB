package com.codeiscoffee.processing.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class Sale {
    private String productType;
    private Double value;

    public Double calculateValue() {
        return value;
    }
}
