package com.bersyte.eventz.features.auth.application.dtos;

import com.bersyte.eventz.features.users.domain.model.UserRole;

import java.time.LocalDateTime;


public record AuthResponse(
    String accessToken,
    String refreshToken,
    LocalDateTime expiresAt,
    String id,
    String email,
    String firstName,
    String lastName,
    UserRole role,
    boolean isEnabled,
    boolean isVerified,
    LocalDateTime createdAt
) {}
