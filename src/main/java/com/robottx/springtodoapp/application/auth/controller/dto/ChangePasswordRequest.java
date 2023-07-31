package com.robottx.springtodoapp.application.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    String token;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;

}
