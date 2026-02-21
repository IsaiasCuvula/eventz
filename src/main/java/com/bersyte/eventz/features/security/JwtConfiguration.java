package com.bersyte.eventz.features.security;

import java.time.Duration;

public interface JwtConfiguration {
    String getJwtSecretKey();
    Duration getAccessTokenExpiration();
    Duration getRefreshTokenExpiration();
}
