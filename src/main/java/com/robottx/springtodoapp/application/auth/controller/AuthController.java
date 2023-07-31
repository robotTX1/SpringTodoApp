package com.robottx.springtodoapp.application.auth.controller;

import com.robottx.springtodoapp.application.auth.controller.dto.*;
import com.robottx.springtodoapp.application.auth.service.AuthService;
import com.robottx.springtodoapp.application.config.ErrorResponse;
import com.robottx.springtodoapp.model.frontend.FrontendConfigProperties;
import com.robottx.springtodoapp.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final FrontendConfigProperties frontendConfig;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage signUp(@RequestBody @Valid SignUpRequest request) {
        authService.signUp(request);

        return new GenericMessage("Successful Sign Up");
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
        String linkBase = "%s%s".formatted(frontendApplicationUrl(), frontendConfig.changePasswordUrl());

        authService.requestPasswordChange(request, linkBase);

        return new GenericMessage("An email has been sent to your email address");
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public GenericMessage changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);

        return new GenericMessage("Successfully changed password");
    }

    private String frontendApplicationUrl() {
        return "http://%s:%d".formatted(frontendConfig.address(), frontendConfig.port());
    }
}
