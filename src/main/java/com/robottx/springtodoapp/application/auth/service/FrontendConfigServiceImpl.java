package com.robottx.springtodoapp.application.auth.service;

import com.robottx.springtodoapp.model.frontend.FrontendConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FrontendConfigServiceImpl implements FrontendConfigService {

    private final FrontendConfigProperties configProperties;

    @Override
    public String getChangePasswordUrl() {
        return "%s%s".formatted(baseUrl(), configProperties.changePasswordUrl());
    }

    @Override
    public String getEmailVerificationUrl() {
        return "%s%s".formatted(baseUrl(), configProperties.emailVerificationUrl());
    }

    private String baseUrl() {
        return "http://%s:%d".formatted(configProperties.address(), configProperties.port());
    }
}
