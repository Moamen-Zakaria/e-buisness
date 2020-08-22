package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid JWT token")
public class InvalidTokenAuthenticationException extends Exception {

    public InvalidTokenAuthenticationException() {
        super("Invalid JWT token");
    }

    public InvalidTokenAuthenticationException(String message) {
        super(message);
    }

}
