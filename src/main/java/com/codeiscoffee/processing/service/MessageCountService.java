package com.codeiscoffee.processing.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class MessageCountService {

    @Getter
    private int successfulMessages = 0;

    public void registerSuccessfulMessage() {
        successfulMessages++;
    }
}
