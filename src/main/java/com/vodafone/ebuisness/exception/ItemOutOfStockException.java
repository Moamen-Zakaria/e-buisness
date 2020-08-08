package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Item Out Of Stock Exception!")
public class ItemOutOfStockException extends Exception {

    public ItemOutOfStockException() {
        super("Item Out Of Stock Exception!");
    }

    public ItemOutOfStockException(String message) {
        super(message);
    }

}
