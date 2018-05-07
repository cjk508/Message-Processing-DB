package com.codeiscoffee.processing.data;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j(topic = "org.codeiscoffee.processing.data.sales")
public class Sales extends HashMap<String, List<Sale>> {

    public Sales() {
        log.info("");
    }

    public Sale addSale(String productType, Double value) {
        Sale sale = new Sale(productType, value);
        log.info(sale.toString());
        this.merge(productType.toUpperCase(), new ArrayList<>(Collections.singletonList(sale)), (v1, v2) -> {
            v1.addAll(v2);
            return v1;
        });
        return sale;
    }
}
