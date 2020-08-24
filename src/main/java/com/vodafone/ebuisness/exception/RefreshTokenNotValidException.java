package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Refresh Token Not Valid!")
public class RefreshTokenNotValidException extends Exception {

    public RefreshTokenNotValidException() {
        super("Refresh Token Not Valid!");
    }

    public RefreshTokenNotValidException(String message) {
        super(message);
    }

}
