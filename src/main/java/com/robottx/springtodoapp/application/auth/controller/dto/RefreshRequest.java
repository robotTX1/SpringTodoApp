package com.robottx.springtodoapp.application.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String refreshToken) {}
