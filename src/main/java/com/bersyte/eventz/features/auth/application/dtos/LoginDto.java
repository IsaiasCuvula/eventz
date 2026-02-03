package com.bersyte.eventz.features.auth.application.dtos;

import com.bersyte.eventz.utils.Utils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginDto(
        @Email(message = "Email is not valid", regexp = Utils.emailRegexp)
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        @Size(min = 6, message = "Password should have min 6 characters")
        String password
) {
}