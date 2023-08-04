package com.robottx.springtodoapp.application.auth.controller;

import com.robottx.springtodoapp.application.auth.controller.dto.*;
import com.robottx.springtodoapp.application.auth.service.AuthService;
import com.robottx.springtodoapp.model.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${security.require-email-verification}")
    private boolean emailVerificationRequired;

    private final AuthService authService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage signUp(@RequestBody @Valid SignUpRequest request) {
        authService.signUp(request);

        String message = emailVerificationRequired ? "An email has been sent to your email address" :
                                                     "Successful Sign Up";

        return new GenericMessage(message);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse refresh(@RequestBody @Valid RefreshRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid RefreshRequest request) {
        authService.logout(request);
    }

    @PostMapping("/logout-all")
    public void logoutAll(User user) {
        authService.logoutAll(user);
    }

    @PostMapping("/request-password-change")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage requestPasswordChange(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.requestPasswordChange(request);

        return new GenericMessage("An email has been sent to your email address");
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);

        return new GenericMessage("Successfully changed password");
    }

    @PostMapping("/request-email-verification")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage requestEmailVerification(@RequestBody @Valid EmailVerificationRequest request) {
        authService.requestEmailVerification(request);

        return new GenericMessage("An email has been sent to your email address");
    }

    @GetMapping("/verify-email")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage verifyEmail(@RequestParam(required = false) String token) {
        authService.verifyEmail(token);

        return new GenericMessage("Email verified");
    }
}
