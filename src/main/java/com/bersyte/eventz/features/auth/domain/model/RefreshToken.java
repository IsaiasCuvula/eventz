package com.bersyte.eventz.features.auth.domain.model;
import java.time.LocalDateTime;

public record RefreshToken(
    String tokenId,
    String userId,
    LocalDateTime expiresAt
  )
{}
