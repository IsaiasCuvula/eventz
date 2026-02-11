package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.auth.domain.service.TokenService;

public class LogoutUseCase implements VoidUseCase<String> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    
    public LogoutUseCase(RefreshTokenRepository refreshTokenRepository, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
    }
    
    @Override
    public void execute(String refreshToken) {
        String tokenId = tokenService.extractTokenId(refreshToken);
        refreshTokenRepository.deleteByTokenId(tokenId);
    }
}
