package com.robottx.springtodoapp.application.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @Email
    @NotBlank
    private String email;

}
