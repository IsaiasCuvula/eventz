package com.bersyte.eventz.security.auth;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record UserRequestDTO(
        @NotBlank(message = "username is required")
        String username,
        @NotBlank(message = "password is required")
        String password,
        @NotBlank(message = "user role is required")
        UserRole role,
        Long createdAt
) {
    public UserRequestDTO {
        createdAt = new Date().getTime();
    }
}
