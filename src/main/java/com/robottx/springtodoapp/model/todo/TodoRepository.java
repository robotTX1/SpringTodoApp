package com.robottx.springtodoapp.model.todo;

import com.robottx.springtodoapp.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.*;

public interface TodoRepository extends JpaRepository<Todo, UUID>, JpaSpecificationExecutor<Todo> {

    Page<Todo> findByUser(User user, Pageable pageable);

    Optional<Todo> findByUserAndId(User user, UUID id);

}
