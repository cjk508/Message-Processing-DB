package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Operator;
import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.data.sales.Sales;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AdjustmentServiceTest {
    @MockBean
    private SalesService salesService;

    @Autowired
    private AdjustmentService sut;

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySalesSet() {
        Sales sales = new Sales();
        Mockito.when(salesService.getSales()).thenReturn(sales);
        sut.processAdjustment("Apple", 10d, Operator.ADD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueInvalid() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", 5, 5d));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        sut.processAdjustment("Apple", -10d, Operator.ADD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionValueBelowZero() {
        Sales sales = new Sales();
        Sale apple = new Sale("Apple", 5, 5d);
        sales.addSale(apple);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        sut.processAdjustment("Apple", 10d, Operator.SUBTRACT);
    }

    @Test
    public void testErrorApplyingAdjustmentsRevertChanges() {
        Sales sales = new Sales();
        Sale apple = new Sale("Apple", 5, 5d);
        sales.addSale(apple);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        try{
            sut.processAdjustment("Apple", 10d, Operator.SUBTRACT);
        }catch(Exception e){
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

        Assert.assertEquals("Change was not reverted.",5d, apple.getValue(), 0.00001);
    }

    @Test
    public void testAddition() {
        Sales sales = new Sales();
        Sale apple = new Sale("Apple", 5, 5d);
        sales.addSale(apple);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        sut.processAdjustment("Apple", 10d, Operator.ADD);
        Assert.assertEquals(15d, apple.getValue(), 0.00001);
    }

    @Test
    public void testMultiplication() {
        Sales sales = new Sales();
        Sale apple = new Sale("Apple", 5, 5d);
        sales.addSale(apple);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        sut.processAdjustment("Apple", 10d, Operator.MULTIPLY);
        Assert.assertEquals(50d, apple.getValue(), 0.00001);
    }

    @Test
    public void testSubtraction() {
        Sales sales = new Sales();
        Sale apple = new Sale("Apple", 5, 15d);
        sales.addSale(apple);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        sut.processAdjustment("Apple", 10d, Operator.SUBTRACT);
        Assert.assertEquals(5d, apple.getValue(), 0.00001);
    }

}