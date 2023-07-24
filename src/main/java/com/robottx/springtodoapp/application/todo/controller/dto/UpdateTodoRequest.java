package com.robottx.springtodoapp.application.todo.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTodoRequest {

    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @NotNull
    private Boolean completed;
    private LocalDateTime deadLine;

}
