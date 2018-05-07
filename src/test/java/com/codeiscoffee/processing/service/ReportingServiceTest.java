package com.codeiscoffee.processing.service;

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
public class ReportingServiceTest {
    @MockBean
    private SalesService salesService;

    @Autowired
    private ReportingService sut;

    @Test
    public void testEmptySalesSet() {
        Mockito.when(salesService.getSales()).thenReturn(new Sales());
        String logs = sut.reportOnSales();
        Assert.assertEquals("", logs);
    }

    @Test
    public void testOneSale() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", 1, 10d));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 1 sale(s), totalling £10.00\n", logs);
    }

    @Test
    public void testMultipleSalesOneProduct() {
        Sales sales = new Sales();
        Sale a1 = new Sale("Apple", 1, 10d);
        Sale a2 = new Sale("Apple", 1, 0.10);

        sales.addSale(a1);
        sales.addSale(a2);
        sales.addSale(a2);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 3 sale(s), totalling £10.20\n", logs);
    }

    @Test
    public void testMultipleSalesMultipleProducts() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", 1, 10d));
        sales.addSale(new Sale("ORaNgE", 1, 0.10));
        sales.addSale(new Sale("Orange", 1, 0.10d));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 1 sale(s), totalling £10.00\nOrange has 2 sale(s), totalling £0.20\n", logs);
    }

    @Test
    public void testMultipleSalesMultipleProductsMultipleOccurrences() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", 20, 10d));
        sales.addSale(new Sale("ORaNgE", 30, 0.10d));
        sales.addSale(new Sale("Orange", 30, 0.10d));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 20 sale(s), totalling £200.00\nOrange has 60 sale(s), totalling £6.00\n", logs);
    }

    @Test
    public void testLargeNumbers() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", Integer.MAX_VALUE, 10.1d));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 2147483647 sale(s), totalling £21689584834.70\n", logs);
    }
}