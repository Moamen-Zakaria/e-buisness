package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED , reason="Email and password don't match!")
public class LoginFailException extends Exception {

    public LoginFailException() {
        super("Email and password don't match!");
    }

    public LoginFailException(String message) {
        super(message);
    }

}
