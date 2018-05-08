package com.codeiscoffee.processing.data.sales;

import com.codeiscoffee.processing.data.ProductValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class Sale extends ProductValue {
    private final int units;

    public Sale(String productType, int units, Double value) {
        super(productType, value);
        this.units = units;
    }

    public Double calculateValue() {
        return getValue() * units;
    }
}
