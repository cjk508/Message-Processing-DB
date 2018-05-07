package com.codeiscoffee.processing.data.sales;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Sale implements Serializable {
    private final String productType;
    private final int units;
    @Setter
    private Double value;

    public Double calculateValue() {
        return value * units;
    }
}
