package com.zijian.java.web.spring.webapp.exceptions;

import java.util.Date;

import com.zijian.java.web.spring.webapp.ui.model.response.ErrorMessage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(final UserServiceException ex, final WebRequest request) {

        String message = "My handler says: " + ex.getMessage();
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message);

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherException(final Exception ex, final WebRequest request) {

        String message = "Unexpected: " + ex.getMessage();
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message);

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
