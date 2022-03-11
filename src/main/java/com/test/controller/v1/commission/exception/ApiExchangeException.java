package com.test.controller.v1.commission.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Api exchange not working")
public class ApiExchangeException extends RuntimeException {
    public ApiExchangeException() {
        super("Api exchange not working", new Throwable(), true, false);
    }
}
