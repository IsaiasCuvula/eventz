package com.bersyte.eventz.features.auth.application.dtos;

import com.bersyte.eventz.features.users.domain.model.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;


public record AuthResponse(
    String accessToken,
    String refreshToken,
    LocalDateTime expiresAt,
    UUID userId,
    String email,
    String firstName,
    String lastName,
    UserRole role,
    boolean isEnabled,
    boolean isVerified,
    LocalDateTime createdAt
) {}
