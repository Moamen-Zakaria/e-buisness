package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT , reason="Email address not registered!")
public class EmailDoesNotExistException extends Exception {

    public EmailDoesNotExistException() {
        super("Email address not registered!");
    }

    public EmailDoesNotExistException(String message) {
        super(message);
    }

}
