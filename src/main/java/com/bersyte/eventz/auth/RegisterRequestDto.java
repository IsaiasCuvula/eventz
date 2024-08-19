package com.bersyte.eventz.auth;

import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.utils.Utils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record RegisterRequestDto(
        @Email(message = "Email is not valid", regexp = Utils.emailRegexp)
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        @Size(min = 6, message = "Password should have min 6 characters")
        String password,
        @NotNull(message = "user role is required")
        UserRole role,
        String token,
        @NotNull(message = "user firstName is required")
        String firstName,
        String lastName,
        String phone,
        Long createdAt
) {
    public RegisterRequestDto {
        createdAt = new Date().getTime();
    }
}
