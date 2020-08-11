package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNSUPPORTED_MEDIA_TYPE , reason="Invalid image format")
public class InvalidImageFormatException extends Exception {

    public InvalidImageFormatException() {
        super("Invalid image format");
    }

    public InvalidImageFormatException(String message) {
        super(message);
    }

}
