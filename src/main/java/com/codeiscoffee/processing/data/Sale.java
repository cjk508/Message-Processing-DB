package com.codeiscoffee.processing.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sale {
    private String productType;
    private Double value;
}
