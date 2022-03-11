package com.test.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "client not found")
public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException() {
        super("Client not found", new Throwable(), true, false);
    }
}
