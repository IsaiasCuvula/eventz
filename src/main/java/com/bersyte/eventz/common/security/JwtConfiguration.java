package com.bersyte.eventz.common.security;

import java.time.Duration;

public interface JwtConfiguration {
    String getJwtSecretKey();
    Duration getAccessTokenExpiration();
    Duration getRefreshTokenExpiration();
}
