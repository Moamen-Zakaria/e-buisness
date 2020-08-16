package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT , reason="No items to checkout exception!")
public class EmptyCartException extends Exception {

    public EmptyCartException() {
        super("No items to checkout exception!");
    }

    public EmptyCartException(String message) {
        super(message);
    }

}