package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.user.User;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateToken(User user);

    boolean validateToken(String accessToken);

    Authentication getAuthentication(String accessToken);
}
