package com.robottx.springtodoapp.application.todo.controller;

import com.robottx.springtodoapp.application.exception.NotFoundException;
import com.robottx.springtodoapp.application.todo.controller.dto.CreateTodoRequest;
import com.robottx.springtodoapp.application.todo.controller.dto.UpdateTodoRequest;
import com.robottx.springtodoapp.application.todo.service.TodoService;
import com.robottx.springtodoapp.application.util.AppUtils;
import com.robottx.springtodoapp.model.todo.Todo;
import com.robottx.springtodoapp.model.todo.TodoFacets;
import com.robottx.springtodoapp.model.todo.TodoVO;
import com.robottx.springtodoapp.model.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<Page<TodoVO>> listTodos(
            User user,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false, defaultValue = "createdAt") String sort,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {

        if(!AppUtils.doesClassContainField(Todo.class, sort)) {
            sort = "createdAt";
        }

        TodoFacets facets = new TodoFacets(search, sort, sortDir, limit, offset);
        return ResponseEntity.ok(todoService.listTodos(user, facets));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoVO> getTodoById(User user, @PathVariable(name = "todoId") UUID todoId) {
        return ResponseEntity.ok(todoService.getTodoById(user, todoId)
                .orElseThrow(() -> new NotFoundException("Can't find Todo with id: " + todoId.toString())));
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(User user, @RequestBody @Valid CreateTodoRequest request) {
        Todo todo = todoService.createTodo(user, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todo.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoVO> updateTodoById(User user,
                                                 @PathVariable("todoId") UUID todoId,
                                                 @RequestBody @Valid UpdateTodoRequest request) {
        TodoVO todoVO = todoService.updateTodoById(user, todoId, request);

        return ResponseEntity.ok(todoVO);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodoById(User user, @PathVariable UUID todoId) {
        todoService.deleteById(user, todoId);

        return ResponseEntity.noContent().build();
    }
}
