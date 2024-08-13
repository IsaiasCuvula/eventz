package com.bersyte.eventz.security.auth;

import java.util.Date;

public record UserResponseDTO(
        String token,
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        Date createdAt,
        UserRole role
) {
}
