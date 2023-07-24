package com.robottx.springtodoapp.application.auth.controller;

import com.robottx.springtodoapp.application.auth.controller.dto.RefreshRequest;
import com.robottx.springtodoapp.model.user.User;
import com.robottx.springtodoapp.application.auth.controller.dto.LoginRequest;
import com.robottx.springtodoapp.application.auth.controller.dto.LoginResponse;
import com.robottx.springtodoapp.application.auth.controller.dto.SignUpRequest;
import com.robottx.springtodoapp.application.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ModelAndView signUp(@RequestBody @Valid SignUpRequest request, HttpServletRequest httpServletRequest) {
        authService.signUp(request);

        LoginRequest loginRequest = new LoginRequest(request.getEmail(), request.getPassword());
        httpServletRequest.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        return new ModelAndView("redirect:/api/v1/auth/login", "user", Map.of("user", loginRequest));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest request) {
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

    @GetMapping("/protected")
    public ResponseEntity<String> protectedResource(User user) {
        return ResponseEntity.ok("Hello from protected: " + user.getUsername());
    }

    @GetMapping("/protected-user")
    public ResponseEntity<String> protectedUserResource(User user) {
        return ResponseEntity.ok("Hello from protected user resource: " + user.getUsername());
    }

    @GetMapping("/protected-admin")
    public ResponseEntity<String> protectedAdminResource(User user) {
        return ResponseEntity.ok("Hello from protected admin resource: " + user.getUsername());
    }
}
