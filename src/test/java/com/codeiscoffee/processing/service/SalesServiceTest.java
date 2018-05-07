package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Sale;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SalesServiceTest {
    @Autowired
    private SalesService sut;

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyProductType() {
        double value = 12d;
        String productType = "";
        sut.registerSale(productType, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        String productType = "Product";
        sut.registerSale(productType, null);
    }

    @Test
    public void testAddingValidSaleData() {
        String productType = "APPLE";
        Double value = 0.10;
        Sale sale = new Sale(productType, value);
        sut.registerSale(productType,value);
        Assert.assertTrue("sale of "+productType+" not found.",sut.getSales().containsKey(productType));
        Assert.assertTrue("sale of "+productType+" not found.",sut.getSales().get(productType).contains(sale));
    }

    @Test
    public void testEmptySalesListOnInit(){
        Assert.assertTrue("",sut.getSales().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueIsInvalid() {
        sut.registerSale("productType", -1.0d);
    }

    @Test
    public void testDifferentCaseSameProduct() {
        String productType = "APPLE";
        Double value = 0.10;
        sut.registerSale(productType,value);
        sut.registerSale(productType.toLowerCase() ,value);
        Assert.assertEquals(2,sut.getSales().get(productType).size());
    }

    @Test
    public void testCanHoldMultipleProducts(){
        String productType1 = "APPLE";
        String productType2 = "ORANGE";
        Double value = 0.10;
        sut.registerSale(productType1,value);
        sut.registerSale(productType2 ,value);
        Assert.assertEquals(2,sut.getSales().size());
    }
}