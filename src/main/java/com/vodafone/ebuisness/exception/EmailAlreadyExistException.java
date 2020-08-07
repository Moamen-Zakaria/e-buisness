package com.vodafone.ebuisness.exception;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT , reason="Email address already exist!")
public class EmailAlreadyExistException extends Exception {

    public EmailAlreadyExistException() {
        super("Email address already exist!");
    }

    public EmailAlreadyExistException(String message) {
        super(message);
    }

}
