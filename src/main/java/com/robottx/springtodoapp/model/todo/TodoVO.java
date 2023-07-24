package com.robottx.springtodoapp.model.todo;

import java.time.LocalDateTime;
import java.util.UUID;

public record TodoVO(UUID id, String title, String description, boolean completed, LocalDateTime deadLine) {

    public TodoVO(Todo todo) {
        this(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getCompleted(), todo.getDeadLine());
    }

}
