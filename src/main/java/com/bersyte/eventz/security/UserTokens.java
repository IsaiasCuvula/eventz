package com.bersyte.eventz.security;

import java.time.LocalDateTime;

public record UserTokens(
        String token, String refreshToken, LocalDateTime expiration
) {
}
