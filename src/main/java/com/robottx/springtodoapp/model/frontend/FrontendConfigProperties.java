package com.robottx.springtodoapp.model.frontend;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("frontend")
public record FrontendConfigProperties(String address, int port, String changePasswordUrl, String emailVerificationUrl) {
}
