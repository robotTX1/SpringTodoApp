package com.robottx.springtodoapp.application.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(@NotBlank String accessToken, @NotBlank String refreshToken) {
}
