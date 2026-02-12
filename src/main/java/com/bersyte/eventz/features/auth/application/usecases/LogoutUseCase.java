package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import com.bersyte.eventz.features.users.application.dtos.LogoutRequest;

public class LogoutUseCase implements VoidUseCase<LogoutRequest> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    
    public LogoutUseCase(RefreshTokenRepository refreshTokenRepository, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
    }
    
    @Override
    public void execute(LogoutRequest request) {
        String tokenId = tokenService.extractTokenId(request.refreshToken());
        refreshTokenRepository.deleteByTokenId(tokenId);
    }
}
