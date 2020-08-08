package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT , reason="Item not in cart!")
public class ItemNotInCartException extends Exception {

    public ItemNotInCartException() {
        super("Item not in cart!");
    }

    public ItemNotInCartException(String message) {
        super(message);
    }

}
