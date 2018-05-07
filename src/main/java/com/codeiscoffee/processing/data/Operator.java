package com.codeiscoffee.processing.data;

import lombok.Getter;

public enum Operator {
    ADD("add", "+") {
        @Override
        public Double apply(Double initialValue, Double adjustment) {
            return initialValue + adjustment;
        }
    },
    MULTIPLY("multiply", "x") {
        @Override
        public Double apply(Double initialValue, Double adjustment) {
            return initialValue * adjustment;
        }
    },
    SUBTRACT("subtract","-") {
        @Override
        public Double apply(Double initialValue, Double adjustment) {

            Double newValue = initialValue - adjustment;
            if(newValue < 0 ){
                throw new IllegalArgumentException("After adjustment the value for this product would be less than 0. All values must remain greater than or equal to 0");
            }
            return newValue;
        }
    };

    private final String restName;

    @Getter
    private final String symbol;

    Operator(String restName, String symbol) {
        this.restName = restName;
        this.symbol = symbol;
    }

    public abstract Double apply(Double initialValue, Double adjustment);

    @Override
    public String toString() {
        return restName;
    }
}
