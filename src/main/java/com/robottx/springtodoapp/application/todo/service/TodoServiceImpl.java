package com.robottx.springtodoapp.application.todo.service;

import com.robottx.springtodoapp.application.exception.NotFoundException;
import com.robottx.springtodoapp.application.todo.controller.dto.CreateTodoRequest;
import com.robottx.springtodoapp.application.todo.controller.dto.UpdateTodoRequest;
import com.robottx.springtodoapp.model.todo.*;
import com.robottx.springtodoapp.model.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public Page<TodoVO> listTodos(User user, TodoFacets facets) {
        Page<Todo> todoPage;
        if(facets.search() != null) {
            TodoSpecification userSpec =
                    new TodoSpecification(new SearchCriteria("user", "=", user));
            TodoSpecification titleSpec =
                    new TodoSpecification(new SearchCriteria("title", ":", facets.search()));
            TodoSpecification descriptionSpec =
                    new TodoSpecification(new SearchCriteria("description", ":", facets.search()));
            todoPage =
                    todoRepository.findAll(Specification
                            .where(userSpec.and(titleSpec.or(descriptionSpec))), facets.getPageable());
        } else {
            todoPage = todoRepository.findByUser(user, facets.getPageable());
        }

        List<TodoVO> content = todoPage.getContent().stream().map(TodoVO::new).toList();

        return new PageImpl<>(content, facets.getPageable(), todoPage.getTotalElements());
    }

    @Override
    public Optional<TodoVO> getTodoById(User user, UUID todoId) {
        return todoRepository.findByUserAndId(user, todoId).map(TodoVO::new);
    }

    @Override
    @Transactional
    public Todo createTodo(User user, CreateTodoRequest request) {
        Todo newTodo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted())
                .deadLine(request.getDeadLine())
                .build();

        newTodo.setUser(user);

        return todoRepository.save(newTodo);
    }

    @Override
    @Transactional
    public void deleteById(User user, UUID todoId) {
        Todo todo = checkIfUserIsOwner(user, todoId);

        if(todo == null)
            throw new NotFoundException("Can't find todo with Id: " + todoId);

        todoRepository.deleteById(todoId);
    }

    @Override
    @Transactional
    public TodoVO updateTodoById(User user, UUID todoId, UpdateTodoRequest request) {
        Todo todo = checkIfUserIsOwner(user, todoId);

        if(todo == null)
            throw new NotFoundException("Can't find todo with Id: " + todoId);

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.getCompleted());
        todo.setDeadLine(request.getDeadLine());

        Todo savedTodo = todoRepository.save(todo);

        return new TodoVO(savedTodo);
    }

    private Todo checkIfUserIsOwner(User user, UUID todoId) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);

        if(todoOptional.isPresent()) {
            Todo todo = todoOptional.get();

            if(todo.getUser().getId().equals(user.getId())) {
                return todo;
            }
        }

        return null;
    }
}
