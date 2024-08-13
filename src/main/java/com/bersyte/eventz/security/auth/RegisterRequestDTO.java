package com.bersyte.eventz.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record RegisterRequestDTO(
        @Email(message = "Email is not valid", regexp = Utilities.emailRegexp)
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        String password,
        @NotNull(message = "user role is required")
        UserRole role,
        Long createdAt
) {
    public RegisterRequestDTO {
        createdAt = new Date().getTime();
    }
}
