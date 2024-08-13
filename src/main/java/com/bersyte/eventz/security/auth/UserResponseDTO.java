package com.bersyte.eventz.security.auth;

import java.util.Date;

public record UserResponseDTO(
        Integer id,
        String username,
        Date createdAt,
        UserRole role
) {
}
