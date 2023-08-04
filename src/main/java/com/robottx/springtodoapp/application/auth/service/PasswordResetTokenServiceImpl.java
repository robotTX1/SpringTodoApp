package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.auth.PasswordResetToken;
import com.robottx.springtodoapp.model.auth.PasswordResetTokenRepository;
import com.robottx.springtodoapp.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Value("${security.password-reset-token.expiryTime}")
    private long expiryTime;

    private final PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    public PasswordResetToken createTokenForUser(User user) {
        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID())
                .expiryTime(LocalDateTime.now().plusSeconds(expiryTime))
                .user(user)
                .build();

        return passwordResetTokenRepository.save(token);
    }

    @Override
    public void deleteTokenByUser(User user) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByUser(user);

        if(tokenOptional.isEmpty()) return;

        passwordResetTokenRepository.delete(tokenOptional.get());
    }

    @Override
    public void deleteToken(PasswordResetToken token) {
        passwordResetTokenRepository.delete(token);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        if(token == null) return Optional.empty();
        return passwordResetTokenRepository.findByToken(UUID.fromString(token));
    }

    @Override
    public boolean validateToken(PasswordResetToken token) {
        return token.getExpiryTime().isAfter(LocalDateTime.now());
    }

}
