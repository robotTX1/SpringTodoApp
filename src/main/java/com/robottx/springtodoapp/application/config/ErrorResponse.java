package com.robottx.springtodoapp.application.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private LocalDateTime timeStamp;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.timeStamp = LocalDateTime.now();
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        this(status, message);
        this.errors = errors;
    }

}
