package com.bersyte.eventz.features.auth.application.dtos;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;

import java.time.LocalDateTime;


public record AuthResponse(
    String accessToken,
    String refreshToken,
    LocalDateTime expiresIn,
    UserResponse user
) {}
