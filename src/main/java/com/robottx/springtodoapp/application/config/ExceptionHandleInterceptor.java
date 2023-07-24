package com.robottx.springtodoapp.application.config;

import com.robottx.springtodoapp.application.auth.exception.AuthException;
import com.robottx.springtodoapp.application.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandleInterceptor {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handle(AuthException e) {
        return createErrorResponseEntity(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException ex) {
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "Failed to read request");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        String errorMessage = "Failed to validate request";

        Map<String, String> errorMap = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, errorMap);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException ex) {
        return createErrorResponseEntity(HttpStatus.BAD_REQUEST, "Invalid argument format");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException ex) {
        return createErrorResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        log.error("Please contact the administrator. An unknown error occurred: {}", e.getMessage(), e);
        return createErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                "An error has occurred. Please contact the administrator.");
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(HttpStatus httpStatus, String message) {
        return createErrorResponseEntity(httpStatus, message, null);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(HttpStatus httpStatus,
                                                                    String message,
                                                                    Map<String, String> errorMap) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus, message, errorMap), httpStatus);
    }
}
