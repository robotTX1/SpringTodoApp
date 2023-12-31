package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.application.auth.controller.dto.*;
import com.robottx.springtodoapp.application.auth.exception.AuthException;
import com.robottx.springtodoapp.application.email.service.EmailService;
import com.robottx.springtodoapp.model.auth.EmailVerificationToken;
import com.robottx.springtodoapp.model.auth.PasswordResetToken;
import com.robottx.springtodoapp.model.auth.RefreshToken;
import com.robottx.springtodoapp.model.user.User;
import com.robottx.springtodoapp.model.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${security.require-email-verification}")
    private boolean emailVerificationRequired;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailVerificationService emailVerificationService;
    private final FrontendConfigService frontendConfigService;
    private final EmailService emailService;

    @Override
    @Transactional
    public User signUp(SignUpRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("This email ('%s') is already in use.".formatted(request.getEmail()));
        }

        User user = createUser(request);
        authorityService.addAuthorityToUser(user, "USER_READ");
        authorityService.addAuthorityToUser(user, "USER_WRITE");

        User savedUser = userRepository.save(user);

        if(emailVerificationRequired) {
            requestEmailVerification(new EmailVerificationRequest(user.getEmail()));
        }

        return savedUser;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException("Invalid email or password");
        }

        if(!user.isVerified() && emailVerificationRequired)
            throw new AuthException("Please verify your email address");

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

    @Override
    @Transactional
    public void requestPasswordChange(ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()) return;

        User user = userOptional.get();

        passwordResetTokenService.deleteTokenByUser(user);
        PasswordResetToken token = passwordResetTokenService.createTokenForUser(user);

        String linkBase = frontendConfigService.getChangePasswordUrl();

        try {
            emailService.sendForgotPasswordMail(user, linkBase + token.getToken());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenService
                .findByToken(request.getToken())
                .orElseThrow(() -> new AuthException("Invalid token"));

        if(!passwordResetTokenService.validateToken(resetToken))
            throw new AuthException("Token is expired, request a new one");

        User user = resetToken.getUser();

        user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
        passwordResetTokenService.deleteToken(resetToken);

        // Logout user
        user.getRefreshTokens().clear();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestEmailVerification(EmailVerificationRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()) return;

        User user = userOptional.get();

        if(user.isVerified())
            throw new AuthException("Email is already verified");

        emailVerificationService.deleteTokenByUser(user);
        EmailVerificationToken token = emailVerificationService.createTokenForUser(user);

        String linkBase = frontendConfigService.getEmailVerificationUrl();

        log.info(linkBase);

        try {
            emailService.sendVerificationEmail(user, linkBase + token.getToken());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationService
                .findByToken(token)
                .orElseThrow(() -> new AuthException("Invalid token"));

        if(!emailVerificationService.validateToken(emailVerificationToken))
            throw new AuthException("Token is expired, request a new one");

        User user = emailVerificationToken.getUser();
        user.setVerified(true);

        emailVerificationService.deleteToken(emailVerificationToken);

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
