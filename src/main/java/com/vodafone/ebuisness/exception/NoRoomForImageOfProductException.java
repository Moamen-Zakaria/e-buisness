package com.vodafone.ebuisness.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "No room for more images for this product!")
public class NoRoomForImageOfProductException extends Exception {

    public NoRoomForImageOfProductException() {
        super("No room for more images for this product!");
    }

    public NoRoomForImageOfProductException(String message) {
        super(message);
    }

}
