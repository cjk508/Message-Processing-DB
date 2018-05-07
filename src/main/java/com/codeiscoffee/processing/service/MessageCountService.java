package com.codeiscoffee.processing.service;

import com.codeiscoffee.processing.data.sales.Sale;
import com.codeiscoffee.processing.data.sales.Sales;
import com.codeiscoffee.processing.validation.ProductValueValidation;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class MessageCountService {

    @Getter
    private int successfulMessages = 0;

    public int registerSuccessfulMessage() {
        return ++successfulMessages;
    }
}
