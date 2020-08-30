package com.vodafone.ebuisness.exceptionhandler;

import com.vodafone.ebuisness.exception.*;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email address already exist!")
    @ExceptionHandler(EmailAlreadyExistException.class)
    public void handleEmailAlreadyExistException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email address not registered!")
    @ExceptionHandler(EmailDoesNotExistException.class)
    public void handleEmailDoesNotExistException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Item not in cart!")
    @ExceptionHandler(ItemNotInCartException.class)
    public void handleItemNotInCartException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "No Such Product exist!")
    @ExceptionHandler(NoSuchProductException.class)
    public void handleNoSuchProductException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Email and password don't match!")
    @ExceptionHandler(LoginFailException.class)
    public void handleLoginFailException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Invalid Id!")
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Item Out Of Stock Exception!")
    @ExceptionHandler(ItemOutOfStockException.class)
    public void handleItemOutOfStockException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "No Such Category Exception!")
    @ExceptionHandler(NoSuchCategoryException.class)
    public void handleNoSuchCategoryException(WebRequest request) {
    }


    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "No room for more images for this product!")
    @ExceptionHandler(NoRoomForImageOfProductException.class)
    public void handleNoRoomForImageOfProduct(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Image doesn't Exist!")
    @ExceptionHandler(ImageDoesNotExistException.class)
    public void handleImageDoesNotExist(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason = "Invalid image format")
    @ExceptionHandler(InvalidImageFormatException.class)
    public void handleInvalidImageFormatException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "File size is too large!")
    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "No items to checkout exception!")
    @ExceptionHandler(EmptyCartException.class)
    public void handleEmptyCartException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid JWT token")
    @ExceptionHandler(InvalidTokenAuthenticationException.class)
    public void handleInvalidTokenAuthenticationException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Refresh Token Not Valid!")
    @ExceptionHandler(RefreshTokenNotValidException.class)
    public void handleRefreshTokenNotValidException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Account not logged in!")
    @ExceptionHandler(NoAuthenticationFoundException.class)
    public void handleNoAuthenticationFoundException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.BAD_GATEWAY, reason = "Sorry there's a problem with the server connection, please try again later")
    @ExceptionHandler(ConnectionErrorException.class)
    public void handleConnectionErrorException(WebRequest request) {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "No such invoice found!")
    @ExceptionHandler(NoSuchInvoiceException.class)
    public void handleNoSuchInvoiceException(WebRequest request) {
    }

}
