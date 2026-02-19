package com.bersyte.eventz.features.auth.domain.repository;

import com.bersyte.eventz.features.auth.domain.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    void deleteByTokenId(String tokenId);
    void saveToken(RefreshToken tokenId);
    void revokeAllSessions(UUID userId);
    Optional<RefreshToken> getTokenById(String tokenId);
}
