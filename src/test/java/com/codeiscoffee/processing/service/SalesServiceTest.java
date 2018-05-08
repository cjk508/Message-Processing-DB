package com.codeiscoffee.processing.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SalesServiceTest {
    @Autowired
    private SalesService sut;

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyProductType() {
        double value = 12d;
        String productType = "";
        sut.registerSale(productType, value, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        String productType = "ProductValue";
        sut.registerSale(productType, null, 1);
    }

    @Test
    public void testEmptySalesListOnInit() {
        Assert.assertTrue("Sales is not empty on start", sut.getSales().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueIsInvalid() {
        sut.registerSale("productType", -1.0d, 1);
    }
}