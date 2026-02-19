package com.bersyte.eventz.features.auth.domain.service;

import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TokenService {
    UUID validateRefreshToken(String refreshToken);
    TokenPair createAndPersistTokens(AuthUser user) ;
    String extractTokenId(String token);
    LocalDateTime extractExpiration(String token);
    void invalidateToken(String targetToken);
}
