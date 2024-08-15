package com.bersyte.eventz.security.auth;

import java.util.Date;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        Date createdAt,
        UserRole role
) {
}
