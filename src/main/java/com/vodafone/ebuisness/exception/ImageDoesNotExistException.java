package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT , reason="Image doesn't Exist!")
public class ImageDoesNotExistException extends Exception {

    public ImageDoesNotExistException() {
        super("Image doesn't Exist!");
    }

    public ImageDoesNotExistException(String message) {
        super(message);
    }

}