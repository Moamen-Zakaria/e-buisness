package com.vodafone.ebuisness.exceptionhandler;

import com.vodafone.ebuisness.exception.EmailAlreadyExistException;
import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.LoginFailException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value= HttpStatus.CONFLICT , reason="Email address already exist!")
    @ExceptionHandler(EmailAlreadyExistException.class)
    public void handleEmailAlreadyExistException(WebRequest request) {
    }

    @ResponseStatus(value= HttpStatus.CONFLICT , reason="Email address not registered!")
    @ExceptionHandler(EmailDoesNotExistException.class)
    public void handleEmailDoesNotExistException(WebRequest request) {
    }

    @ResponseStatus(value= HttpStatus.UNAUTHORIZED , reason="Email and password don't match!")
    @ExceptionHandler(LoginFailException.class)
    public void handleLoginFailException(WebRequest request) {
    }

}