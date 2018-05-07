package com.codeiscoffee.processing.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class Sale {
    private String productType;
    private Double value;
    private int occurrences;

    public Double calculateValue() {
        return value*occurrences;
    }
}
