package com.test.controller.v1.commission.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Currency not found")
public class ExchangeCurrencyException extends RuntimeException {
    public ExchangeCurrencyException() {
        super("Currency not found", new Throwable(), true, false);
    }
}
