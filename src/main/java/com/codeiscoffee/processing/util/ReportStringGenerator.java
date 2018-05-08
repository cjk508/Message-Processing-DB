package com.codeiscoffee.processing.util;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReportStringGenerator {

    private ReportStringGenerator() {
    }

    public static String generateNumberString(Double figureToBeRounded, boolean roundTo2DP) {
        BigDecimal bdVal = BigDecimal.valueOf(figureToBeRounded);
        if (roundTo2DP) {
            bdVal = bdVal.setScale(2, RoundingMode.HALF_EVEN);
        } else {
            //always show at least 2 decimal places
            bdVal = bdVal.setScale(Math.max(2, bdVal.scale()), RoundingMode.HALF_EVEN);
        }
        return bdVal.toPlainString();
    }

}
