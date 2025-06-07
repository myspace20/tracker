package com.task.tracker.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFound ex, WebRequest request) {
        return new ResponseEntity<>(
                new ApiErrorResponse(LocalDateTime.now(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        request.getDescription(false)
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                new ApiErrorResponse(LocalDateTime.now(),
                        ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        request.getDescription(false)
                ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
