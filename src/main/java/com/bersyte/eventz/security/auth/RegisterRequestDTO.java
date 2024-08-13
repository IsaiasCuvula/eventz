package com.bersyte.eventz.security.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record RegisterRequestDTO(
        @NotBlank(message = "username is required")
        String username,
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
