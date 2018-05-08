package com.codeiscoffee.processing.data.adjustment;

import com.codeiscoffee.processing.data.Operator;
import com.codeiscoffee.processing.data.ProductValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Adjustment extends ProductValue {
    private Operator operator;

    @Setter
    private int affectedSales = 0;

    public Adjustment(String productType, Operator operator, Double value) {
        super(productType, value);
        this.operator = operator;
    }

    public void incrementAffectedSales(int units) {
        affectedSales += units;
    }
}
