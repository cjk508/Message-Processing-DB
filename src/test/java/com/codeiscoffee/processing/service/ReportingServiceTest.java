package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Sale;
import com.codeiscoffee.processing.data.Sales;
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
        sales.addSale(new Sale("Apple", 10d, 1));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 1 sale(s), totalling £10.0\n", logs);
    }

    @Test
    public void testMultipleSalesOneProduct() {
        Sales sales = new Sales();
        Sale a1 = new Sale("Apple", 10d, 1);
        Sale a2 = new Sale("Apple", 0.10d, 1);

        sales.addSale(a1);
        sales.addSale(a2);
        sales.addSale(a2);
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 3 sale(s), totalling £10.2\n", logs);
    }

    @Test
    public void testMultipleSalesMultipleProducts() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", 10d, 1));
        sales.addSale(new Sale("ORaNgE", 0.10d, 1));
        sales.addSale(new Sale("Orange", 0.10d, 1));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 1 sale(s), totalling £10.0\nOrange has 2 sale(s), totalling £0.2\n", logs);
    }

    @Test
    public void testMultipleSalesMultipleProductsMultipleOccurrences() {
        Sales sales = new Sales();
        sales.addSale(new Sale("Apple", 10d, 20));
        sales.addSale(new Sale("ORaNgE", 0.10d, 30));
        sales.addSale(new Sale("Orange", 0.10d, 30));
        Mockito.when(salesService.getSales()).thenReturn(sales);
        String logs = sut.reportOnSales();
        Assert.assertEquals("Apple has 20 sale(s), totalling £200.0\nOrange has 60 sale(s), totalling £6.0\n", logs);
    }
}