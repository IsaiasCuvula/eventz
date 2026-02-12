package com.bersyte.eventz.features.auth.domain.model;

import java.time.LocalDateTime;

public record TokenPair(
        String token, String refreshToken, LocalDateTime expiration
) {
}
