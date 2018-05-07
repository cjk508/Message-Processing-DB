package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.adjustment.Adjustment;
import com.codeiscoffee.processing.data.sales.Sale;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j(topic = "org.codeiscoffee")
public class ReportingService {
    private SalesService salesService;
    private AdjustmentService adjustmentService;
    private MessageCountService messageCountService;

    public String reportOnSales() {
        StringBuilder builder = new StringBuilder();
        log.info("Sales Report after " + messageCountService.getSuccessfulMessages() + " messages");
        salesService.getSales().forEach((product, sales) -> {
            int totalSales = sales.stream().mapToInt(Sale::getUnits).sum();
            Double totalValue = sales.stream().mapToDouble(Sale::calculateValue).sum();
            BigDecimal bdVal = BigDecimal.valueOf(totalValue);
            bdVal = bdVal.setScale(2, RoundingMode.HALF_EVEN);
            String logMessage = WordUtils.capitalizeFully(product) + " has " + totalSales + " sale(s), totalling £" + bdVal.toPlainString();
            log.info(logMessage);
            builder.append(logMessage);
            builder.append("\n");
        });
        return builder.toString();
    }


    public String reportOnAdjustments() {
        StringBuilder builder = new StringBuilder();
        log.info("Adjustments report after " + messageCountService.getSuccessfulMessages() + " messages");

        adjustmentService.getAdjustments().forEach((product, adj) ->
                builder.append(generateReportForProduct(product, adj))
        );

        if (adjustmentService.getAdjustments().isEmpty()) {
            String emptyMessage = "None of the messages were valid adjustments";
            log.info(emptyMessage);
            builder.append(emptyMessage);
        }
        return builder.toString();
    }

    private String generateReportForProduct(String product, LinkedList<Adjustment> adjustments) {
        StringBuilder builder = new StringBuilder();
        adjustments.forEach(adj -> {
            String logMessage = WordUtils.capitalizeFully(product) + " first " + adj.getAffectedSales() + " sale(s) adjusted by " + adj.getOperator().getSymbol() + adj.getValue();
            log.info(logMessage);
            builder.append(logMessage);
            builder.append("\n");
        });
        return builder.toString();
    }
}
