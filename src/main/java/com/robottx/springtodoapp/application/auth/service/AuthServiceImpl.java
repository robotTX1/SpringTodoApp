package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.application.auth.controller.dto.LoginRequest;
import com.robottx.springtodoapp.application.auth.controller.dto.LoginResponse;
import com.robottx.springtodoapp.application.auth.controller.dto.RefreshRequest;
import com.robottx.springtodoapp.application.auth.controller.dto.SignUpRequest;
import com.robottx.springtodoapp.application.auth.exception.AuthException;
import com.robottx.springtodoapp.model.auth.RefreshToken;
import com.robottx.springtodoapp.model.user.User;
import com.robottx.springtodoapp.model.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User signUp(SignUpRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("This email ('%s') is already in use.".formatted(request.getEmail()));
        }

        User user = createUser(request);
        authorityService.addAuthorityToUser(user, "USER_READ");
        authorityService.addAuthorityToUser(user, "USER_WRITE");

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.generateToken();

        user.addRefreshToken(refreshToken);

        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken.getToken().toString());
    }

    @Override
    @Transactional
    public LoginResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService
                .findByToken(UUID.fromString(request.refreshToken()))
                .orElseThrow(() -> new AuthException("Could not find Refresh Token"));

        User user = refreshToken.getUser();

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            user.removeRefreshToken(refreshToken);

            userRepository.save(user);

            throw new AuthException("Refresh Token is expired");
        }

        String accessToken = jwtService.generateToken(user);

        return new LoginResponse(accessToken, refreshToken.getToken().toString());
    }

    @Override
    @Transactional
    public void logout(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService
                .findByToken(UUID.fromString(request.refreshToken()))
                .orElseThrow(() -> new AuthException("You are already logged out"));

        User user = refreshToken.getUser();

        user.removeRefreshToken(refreshToken);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void logoutAll(User user) {
        if(user.getRefreshTokens().isEmpty()) {
            throw new AuthException("You are already logged out everywhere");
        }

        user.getRefreshTokens().clear();

        userRepository.save(user);
    }

    private User createUser(SignUpRequest request) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }
}
