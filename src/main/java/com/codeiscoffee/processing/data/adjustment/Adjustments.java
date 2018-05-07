package com.codeiscoffee.processing.data.adjustment;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;

@Slf4j(topic = "org.codeiscoffee.processing.data.sales")
public class Adjustments extends HashMap<String, LinkedList<Adjustment>> {

    public void queueAdjustment(Adjustment adjustment) {
        String upperCaseKey = adjustment.getProductType().toUpperCase();
        LinkedList<Adjustment> adjustmentForProduct = this.getOrDefault(upperCaseKey, new LinkedList<>());
        adjustmentForProduct.push(adjustment);
        this.put(upperCaseKey, adjustmentForProduct);
    }
}
