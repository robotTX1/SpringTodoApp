package com.robottx.springtodoapp.application.todo.service;

import com.robottx.springtodoapp.application.todo.controller.dto.CreateTodoRequest;
import com.robottx.springtodoapp.application.todo.controller.dto.UpdateTodoRequest;
import com.robottx.springtodoapp.model.todo.Todo;
import com.robottx.springtodoapp.model.todo.TodoFacets;
import com.robottx.springtodoapp.model.todo.TodoVO;
import com.robottx.springtodoapp.model.user.User;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface TodoService {
    Page<TodoVO> listTodos(User user, TodoFacets facets);

    Optional<TodoVO> getTodoById(User user, UUID todoId);

    Todo createTodo(User user, CreateTodoRequest request);

    void deleteById(User user, UUID todoId);

    TodoVO updateTodoById(User user, UUID todoId, UpdateTodoRequest request);
}
