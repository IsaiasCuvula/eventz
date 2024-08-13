package com.bersyte.eventz.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



public record LoginRequestDTO(
        @Email(message = "Email is not valid", regexp = Utilities.emailRegexp)
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        String password
) {
}