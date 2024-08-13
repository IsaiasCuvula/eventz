package com.bersyte.eventz.security.auth;

import java.util.Date;

public record AuthResponse(
        String token,
        Integer id,
        String email,
        Date createdAt,
        UserRole role
) {
}
