package com.bersyte.eventz.security.auth;

import java.util.Date;

public record AuthResponse(
        String token,
        Integer id,
        String username,
        Date createdAt,
        UserRole role
) {
}
