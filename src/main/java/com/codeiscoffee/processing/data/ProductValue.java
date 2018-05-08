package com.codeiscoffee.processing.data;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class ProductValue implements Serializable {
    private final String productType;
    @Setter
    private Double value;
}
