package com.robottx.springtodoapp.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    boolean existsByAuthority(String authority);

    Optional<Authority> findByAuthority(String authority);

    void deleteByAuthority(String name);
}
