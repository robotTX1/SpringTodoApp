package com.robottx.springtodoapp.model.todo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record TodoFacets(String search, String sort, String sortDir, int limit, int offset) {
    public TodoFacets {
        if(offset < 0) {
            offset = 0;
        }

        if(limit < 0 || limit > 100) {
            limit = 10;
        }
    }

    public Pageable getPageable() {
        if(sortDir.equalsIgnoreCase("desc"))
            return PageRequest.of(offset, limit, Sort.by(sort).descending());

        return PageRequest.of(offset, limit, Sort.by(sort).ascending());
    }
}
