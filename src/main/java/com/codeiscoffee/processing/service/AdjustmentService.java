package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.Operator;
import com.codeiscoffee.processing.data.adjustment.Adjustment;
import com.codeiscoffee.processing.data.adjustment.Adjustments;
import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.validation.ProductValueValidation;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdjustmentService implements ProductValueValidation {

    @Getter
    private Adjustments adjustments;

    private SalesService salesService;

    @Autowired
    public AdjustmentService(SalesService salesService) {
        this.salesService = salesService;
        this.adjustments = new Adjustments();
    }

    public Adjustment processAdjustment(String productType, Double value, Operator operator) {
        validateProductType(productType);
        validateValue(value);
        Adjustment adjustment = new Adjustment(productType, operator, value);
        adjustSales(adjustment);
        adjustments.queueAdjustment(adjustment);
        return adjustment;
    }

    private void adjustSales(Adjustment adjustment) {
        String upperCaseKey = adjustment.getProductType().toUpperCase();
        if (salesService.getSales().containsKey(upperCaseKey)) {
            List<Sale> sales = salesService.getSales().get(upperCaseKey);
            byte[] salesBackup = SerializationUtils.serialize(new ArrayList<>(sales));
            try {
                sales.forEach(sale -> {
                    Double currentValue = sale.getValue();
                    Double newValue = adjustment.getOperator().apply(currentValue, adjustment.getValue());
                    sale.setValue(newValue);
                    adjustment.incrementAffectedSales(sale.getUnits());
                });
            }
            catch(Exception e){
                List<Sale> revertChanges = (List<Sale>) SerializationUtils.deserialize(salesBackup);
                sales.clear();
                sales.addAll(revertChanges);
                throw e;
            }
        } else {
            throw new IllegalArgumentException("ProductType is not present in sales. Cannot make adjustment");
        }
    }
}
