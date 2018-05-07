package com.codeiscoffee.processing.exceptions;

public class MessageLimitException extends RuntimeException {
    public MessageLimitException(int successfulMessages) {
        super("There have been " + successfulMessages + " successfully processed messages. Therefore this process has been paused.");
    }
}
