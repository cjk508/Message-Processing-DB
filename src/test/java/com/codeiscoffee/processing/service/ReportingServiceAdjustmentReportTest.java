package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Operator;
import com.codeiscoffee.processing.data.adjustment.Adjustment;
import com.codeiscoffee.processing.data.adjustment.Adjustments;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReportingServiceAdjustmentReportTest {
    @MockBean
    private AdjustmentService adjustmentService;

    @Autowired
    private ReportingService sut;

    @Test
    public void testEmptyAdjustments() {
        Mockito.when(adjustmentService.getAdjustments()).thenReturn(new Adjustments());
        String log = sut.reportOnAdjustments();
        Assert.assertFalse(StringUtils.isEmpty(log));
        Assert.assertEquals("None of the messages were valid adjustments", log);
    }

    @Test
    public void testOneAdjustment() {
        Adjustments adjustments = new Adjustments();
        double value = 7d;
        String productType = "Apple";
        Operator operator = Operator.ADD;
        adjustments.queueAdjustment(new Adjustment(productType, operator, value));

        Mockito.when(adjustmentService.getAdjustments()).thenReturn(adjustments);
        String log = sut.reportOnAdjustments();
        Assert.assertEquals("Apple first 0 sale(s) adjusted by +7.00\n", log);
    }

    @Test
    public void testMultipleAdjustmentsOneProduct() {
        Adjustments adjustments = new Adjustments();
        double v1 = 7d;
        Operator o1 = Operator.ADD;
        double v2 = 7123d;
        Operator o2 = Operator.MULTIPLY;
        String productType = "Apple";

        adjustments.queueAdjustment(new Adjustment(productType, o1, v1));
        adjustments.queueAdjustment(new Adjustment(productType, o2, v2));

        Mockito.when(adjustmentService.getAdjustments()).thenReturn(adjustments);
        String log = sut.reportOnAdjustments();
        Assert.assertEquals("Apple first 0 sale(s) adjusted by x7123.00\n" +
                "Apple first 0 sale(s) adjusted by +7.00\n", log);
    }
}