package com.codeiscoffee.processing.data.adjustment;

import com.codeiscoffee.processing.data.Operator;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Adjustment {
    private String productType;
    private Operator operator;
    private Double value;

    @Setter
    private int affectedSales = 0;

    public Adjustment(String productType, Operator operator, Double value) {
        this.productType = productType;
        this.operator = operator;
        this.value = value;
    }

    public void incrementAffectedSales(int units) {
        affectedSales += units;
    }
}
