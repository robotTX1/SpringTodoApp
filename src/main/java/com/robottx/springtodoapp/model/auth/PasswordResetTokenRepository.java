package com.robottx.springtodoapp.model.auth;

import com.robottx.springtodoapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByUser(User user);

    Optional<PasswordResetToken> findByToken(UUID token);

}
