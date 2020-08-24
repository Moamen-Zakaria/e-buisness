package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Account not logged in!")
public class NoAuthenticationFoundException extends Exception {

    public NoAuthenticationFoundException() {
        super("Account not logged in!");
    }

    public NoAuthenticationFoundException(String message) {
        super(message);
    }

}
