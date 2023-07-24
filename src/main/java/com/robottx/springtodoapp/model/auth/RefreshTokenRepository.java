package com.robottx.springtodoapp.model.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    boolean existsByToken(UUID token);

    Optional<RefreshToken> findByToken(UUID token);
}
