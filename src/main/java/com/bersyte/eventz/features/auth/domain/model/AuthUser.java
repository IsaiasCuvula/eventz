package com.bersyte.eventz.features.auth.domain.model;

import com.bersyte.eventz.features.users.domain.model.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuthUser(
        UUID userId,
        String email,
        String password,
        String firstName,
        String lastName,
        UserRole role,
        boolean isEnabled,
        boolean isVerified,
        LocalDateTime createdAt
) {
}
