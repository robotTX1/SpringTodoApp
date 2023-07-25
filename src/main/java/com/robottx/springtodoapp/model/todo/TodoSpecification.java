package com.robottx.springtodoapp.model.todo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class TodoSpecification implements Specification<Todo> {

    private final SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(
            @NonNull Root<Todo> root,
            @NonNull CriteriaQuery<?> query,
            @NonNull CriteriaBuilder builder) {

        if(searchCriteria.operation().equalsIgnoreCase("=")) {
            return builder.equal(root.get(searchCriteria.key()), searchCriteria.value());
        } else if(searchCriteria.operation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.get(searchCriteria.key()), searchCriteria.value().toString());
        } else if(searchCriteria.operation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.get(searchCriteria.key()), searchCriteria.value().toString());
        } else if(searchCriteria.operation().equalsIgnoreCase(":")) {
            if(root.get(searchCriteria.key()).getJavaType() == String.class) {
                return builder.like(builder.lower(root.get(searchCriteria.key())), "%" + searchCriteria.value().toString().toLowerCase() + "%");
            } else {
                return builder.equal(root.get(searchCriteria.key()), searchCriteria.value());
            }
        }

        return null;
    }
}
