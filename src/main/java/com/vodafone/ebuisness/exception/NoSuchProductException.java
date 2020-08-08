package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "No Such Product exist!")
public class NoSuchProductException extends Exception {

    public NoSuchProductException() {
        super("No Such Product exist!");
    }

    public NoSuchProductException(String message) {
        super(message);
    }

}
