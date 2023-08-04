package com.robottx.springtodoapp.model.auth;

import com.robottx.springtodoapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByUser(User user);

    Optional<EmailVerificationToken> findByToken(UUID token);

}
