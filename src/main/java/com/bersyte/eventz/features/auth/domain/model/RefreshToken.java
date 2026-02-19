package com.bersyte.eventz.features.auth.domain.model;
import java.time.LocalDateTime;
import java.util.UUID;

public record RefreshToken(
    String tokenId,
    UUID userId,
    LocalDateTime expiresAt
  )
{}
