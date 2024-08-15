package com.bersyte.eventz.users;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDTO(
        @NotBlank(message = "first name is required")
        String firstName,
        @NotBlank(message = "last name is required")
        String lastName,
        String phone
) {
}
