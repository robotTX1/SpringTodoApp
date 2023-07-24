package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.application.auth.controller.dto.LoginRequest;
import com.robottx.springtodoapp.application.auth.controller.dto.LoginResponse;
import com.robottx.springtodoapp.application.auth.controller.dto.RefreshRequest;
import com.robottx.springtodoapp.application.auth.controller.dto.SignUpRequest;
import com.robottx.springtodoapp.model.user.User;

public interface AuthService {
    User signUp(SignUpRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse refresh(RefreshRequest request);

    void logout(RefreshRequest request);

    void logoutAll(User user);
}
