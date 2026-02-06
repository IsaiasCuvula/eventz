package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.LoginRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.AuthService;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import com.bersyte.eventz.features.users.domain.model.AppUser;

public class LoginUseCase implements UseCase<LoginRequest, AuthResponse> {
    private final AuthService authService;
    private final AuthMapper authMapper;
    private final TokenService tokenService;
    
    public LoginUseCase(
            AuthService authService, AuthMapper authMapper, TokenService tokenService
    ) {
        this.authService = authService;
        this.authMapper = authMapper;
        this.tokenService = tokenService;
    }
    
    
    @Override
    public AuthResponse execute(LoginRequest request) {
        AppUser authenticatedUser = authService.authenticate(request.email(), request.password());
        TokenPair tokens = tokenService.createUserTokens(authenticatedUser.getEmail());
        return authMapper.toResponse(tokens, authenticatedUser);
    }
}
