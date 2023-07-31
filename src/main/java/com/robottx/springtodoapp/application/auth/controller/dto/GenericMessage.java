package com.robottx.springtodoapp.application.auth.controller.dto;

import java.time.LocalDateTime;

public record GenericMessage(String message, LocalDateTime timestamp) {

    public GenericMessage(String message) {
        this(message, LocalDateTime.now());
    }

}