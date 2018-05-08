package com.codeiscoffee.processing.data.adjustment;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.HashMap;

@Slf4j(topic = "org.codeiscoffee.processing.data.sales")
public class Adjustments extends HashMap<String, ArrayDeque<Adjustment>> {

    public void queueAdjustment(Adjustment adjustment) {
        String upperCaseKey = adjustment.getProductType().toUpperCase();
        ArrayDeque<Adjustment> adjustmentForProduct = this.getOrDefault(upperCaseKey, new ArrayDeque<>());
        adjustmentForProduct.push(adjustment);
        this.put(upperCaseKey, adjustmentForProduct);
    }
}
