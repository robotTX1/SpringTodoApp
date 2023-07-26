package com.robottx.springtodoapp.application.image.exception;

public class InvalidImageException extends RuntimeException {

    public InvalidImageException() {
    }

    public InvalidImageException(String message) {
        super(message);
    }

    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageException(Throwable cause) {
        super(cause);
    }

    public InvalidImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
