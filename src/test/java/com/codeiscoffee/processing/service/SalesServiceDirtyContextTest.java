package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Sale;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesServiceDirtyContextTest {
    @Autowired
    private SalesService sut;

    @Test
    public void testAddingValidSaleData() {
        String productType = "APPLE";
        Double value = 0.10;
        Sale sale = new Sale(productType, value,1);
        sut.registerSale(productType, value, 1);
        Assert.assertTrue("sale of " + productType + " not found.", sut.getSales().containsKey(productType));
        Assert.assertTrue("sale of " + productType + " not found.", sut.getSales().get(productType).contains(sale));
    }

    @Test
    public void testDifferentCaseSameProduct() {
        String productType = "APPLE";
        Double value = 0.10;
        sut.registerSale(productType, value, 1);
        sut.registerSale(productType.toLowerCase(), value, 1);
        Assert.assertEquals(2, sut.getSales().get(productType).size());
    }

    @Test
    public void testCanHoldMultipleProducts() {
        String productType1 = "APPLE";
        String productType2 = "ORANGE";
        Double value = 0.10;
        sut.registerSale(productType1, value, 1);
        sut.registerSale(productType2, value, 1);
        Assert.assertEquals(2, sut.getSales().size());
    }
}