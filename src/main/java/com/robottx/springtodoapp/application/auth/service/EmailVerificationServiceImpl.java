package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.auth.EmailVerificationToken;
import com.robottx.springtodoapp.model.auth.EmailVerificationTokenRepository;
import com.robottx.springtodoapp.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Value("${security.email-verification-token.expiryTime}")
    private long expiryTime;

    private final EmailVerificationTokenRepository repository;

    @Override
    public EmailVerificationToken createTokenForUser(User user) {
        EmailVerificationToken token = EmailVerificationToken.builder()
                .token(UUID.randomUUID())
                .expiryTime(LocalDateTime.now().plusSeconds(expiryTime))
                .user(user)
                .build();

        return repository.save(token);
    }

    @Override
    public void deleteTokenByUser(User user) {
        Optional<EmailVerificationToken> optionalToken = repository.findByUser(user);

        if(optionalToken.isEmpty()) return;

        repository.delete(optionalToken.get());
    }

    @Override
    public void deleteToken(EmailVerificationToken token) {
        repository.delete(token);
    }

    @Override
    public Optional<EmailVerificationToken> findByToken(String token) {
        if(token == null) return Optional.empty();

        try {
            return repository.findByToken(UUID.fromString(token));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Override
    public boolean validateToken(EmailVerificationToken token) {
        return token.getExpiryTime().isAfter(LocalDateTime.now());
    }
}
