package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_GATEWAY , reason="Sorry there's a problem with the server connection, please try again later")
public class ConnectionErrorException extends Exception {

    public ConnectionErrorException() {
        super("Sorry there's a problem with the server connection, please try again later");
    }

    public ConnectionErrorException(String message) {
        super(message);
    }

}
