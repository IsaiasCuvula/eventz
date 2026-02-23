package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.auth.application.dtos.RevokeAllTokensRequest;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class RevokeAllTokensUseCase implements VoidUseCase<RevokeAllTokensRequest> {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserValidationService userValidationService;
    
    public RevokeAllTokensUseCase(RefreshTokenRepository refreshTokenRepository, UserValidationService userValidationService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userValidationService = userValidationService;
    }
    
    @Transactional
    @Override
    public void execute(RevokeAllTokensRequest request) {
        UUID targetId = request.targetId();
        AppUser requester = userValidationService.getRequesterById(request.requesterId());
        if(!requester.canRevokeTokens(targetId)){
            throw new UnauthorizedException("You're not allowed to revoke tokens for this user");
        }
        refreshTokenRepository.revokeAllTokens(targetId);
    }
}
