package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Sale;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j(topic = "org.codeiscoffee")
public class ReportingService {
    private SalesService salesService;

    public String reportOnSales() {
        StringBuilder builder = new StringBuilder();
        log.info("Sales Report after "+salesService.getSuccessfulMessages()+" messages");
        salesService.getSales().forEach((product, sales) ->{
            int totalSales = sales.stream().mapToInt(Sale::getOccurrences).sum();
            Double totalValue = sales.stream().mapToDouble(Sale::calculateValue).sum();
            String logMessage = WordUtils.capitalizeFully(product) +" has "+totalSales+" sale(s), totalling £"+totalValue;
            log.info(logMessage);
            builder.append(logMessage);
            builder.append("\n");
        });
        return builder.toString();
    }
}
