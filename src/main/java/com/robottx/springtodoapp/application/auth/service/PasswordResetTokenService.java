package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.auth.PasswordResetToken;
import com.robottx.springtodoapp.model.user.User;

import java.util.Optional;

public interface PasswordResetTokenService {
    PasswordResetToken createTokenForUser(User user);

    void deleteTokenByUser(User user);

    void deleteToken(PasswordResetToken token);

    Optional<PasswordResetToken> findByToken(String token);

    boolean validateToken(PasswordResetToken token);
}
