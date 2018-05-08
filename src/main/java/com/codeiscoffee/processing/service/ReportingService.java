package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.adjustment.Adjustment;
import com.codeiscoffee.processing.data.sales.Sale;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Iterator;

import static com.codeiscoffee.processing.util.ReportStringGenerator.generateNumberString;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j(topic = "org.codeiscoffee")
public class ReportingService {
    private SalesService salesService;
    private AdjustmentService adjustmentService;
    private MessageCountService messageCountService;

    public void printReportsWhenNecessary(){
        if (messageCountService.getSuccessfulMessages() % 10 == 0) {
            reportOnSales();
        }
        if (messageCountService.getSuccessfulMessages() == 50) {
            log.info("Service has processed its 50th message. The process will now pause and stop accepting new messages.");
            reportOnAdjustments();
        }
    }


    protected String reportOnSales() {
        StringBuilder builder = new StringBuilder();
        log.info("Sales Report after " + messageCountService.getSuccessfulMessages() + " messages");
        salesService.getSales().forEach((product, sales) -> {
            int totalSales = sales.stream().mapToInt(Sale::getUnits).sum();
            Double totalValue = sales.stream().mapToDouble(Sale::calculateValue).sum();
            String logMessage = WordUtils.capitalizeFully(product) + " has " + totalSales + " sale(s), totalling £" + generateNumberString(totalValue, true);
            logMessageAndBuildString(builder, logMessage);
        });
        return builder.toString();
    }


    protected String reportOnAdjustments() {
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

    private String generateReportForProduct(String product, ArrayDeque<Adjustment> adjustments) {
        StringBuilder builder = new StringBuilder();
        Iterator<Adjustment> iterator = adjustments.descendingIterator();
        while(iterator.hasNext()){
            Adjustment adj = iterator.next();
            String logMessage = WordUtils.capitalizeFully(product) + " first " + adj.getAffectedSales() + " sale(s) adjusted by " + adj.getOperator().getSymbol() + generateNumberString(adj.getValue(), false);
            logMessageAndBuildString(builder, logMessage);
        }
        
        return builder.toString();
    }

    private void logMessageAndBuildString(StringBuilder builder, String logMessage) {
        log.info(logMessage);
        builder.append(logMessage);
        builder.append("\n");
    }
}
