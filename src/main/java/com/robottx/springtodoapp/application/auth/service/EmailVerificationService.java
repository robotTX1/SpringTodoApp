package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.auth.EmailVerificationToken;
import com.robottx.springtodoapp.model.user.User;

import java.util.Optional;

public interface EmailVerificationService {

    EmailVerificationToken createTokenForUser(User user);

    void deleteTokenByUser(User user);

    void deleteToken(EmailVerificationToken token);

    Optional<EmailVerificationToken> findByToken(String token);

    boolean validateToken(EmailVerificationToken token);

}
