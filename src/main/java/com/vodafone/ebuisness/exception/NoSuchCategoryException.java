package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "No Such Category Exception!")
public class NoSuchCategoryException extends Exception {

    public NoSuchCategoryException() {
        super("No Such Category Exception!");
    }

    public NoSuchCategoryException(String message) {
        super(message);
    }


}
