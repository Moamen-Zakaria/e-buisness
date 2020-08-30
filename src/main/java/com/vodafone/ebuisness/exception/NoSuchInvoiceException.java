package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "No such invoice found!")
public class NoSuchInvoiceException extends Exception {

    public NoSuchInvoiceException() {
        super("No such invoice found!");
    }

    public NoSuchInvoiceException(String message) {
        super(message);
    }


}
