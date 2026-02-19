package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import com.bersyte.eventz.features.users.application.dtos.RefreshTokenRequest;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class RefreshTokenUseCase implements UseCase<RefreshTokenRequest, AuthResponse> {
    private final TokenService tokenService;
    private final UserValidationService userValidationService;
    private final AuthMapper authMapper;
    
    public RefreshTokenUseCase(TokenService tokenService, UserValidationService userValidationService, AuthMapper authMapper) {
        this.tokenService = tokenService;
        this.userValidationService = userValidationService;
        this.authMapper = authMapper;
    }
    
    @Transactional
    @Override
    public AuthResponse execute(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        UUID userId = tokenService.validateRefreshToken(refreshToken);
        AppUser appUser = userValidationService.getRequesterById(userId);
        AuthUser authUser = authMapper.fromAppUser(appUser);
        tokenService.invalidateToken(refreshToken);
        TokenPair tokens = tokenService.createAndPersistTokens(authUser);
        return authMapper.toResponse(tokens, authUser);
    }
}
