package com.bersyte.eventz.features.auth.domain.service;

import com.bersyte.eventz.features.auth.domain.model.TokenPair;

public interface TokenService {
    String refreshToken(String oldToken);
    TokenPair createUserTokens(String userId);
}
