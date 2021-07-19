package com.moneyroomba.service.exception.Currency;

public class NoCurrencysFoundException extends RuntimeException {

    public NoCurrencysFoundException(String message) {
        super(message);
    }
}
