package com.test.controller.v1.commission.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "report mandatory field missing")
public class CommissionException extends RuntimeException {
    public CommissionException() {
        super("Commission request mandatory field missing", new Throwable(), true, false);
    }
}
