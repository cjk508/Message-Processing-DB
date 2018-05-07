package com.codeiscoffee.processing.data.sales;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j(topic = "org.codeiscoffee.processing.data.sales")
public class Sales extends HashMap<String, List<Sale>> {

    public void addSale(Sale sale) {
        log.info(sale.toString());
        this.merge(sale.getProductType().toUpperCase(), new ArrayList<>(Collections.singletonList(sale)), (v1, v2) -> {
            v1.addAll(v2);
            return v1;
        });
    }
}
