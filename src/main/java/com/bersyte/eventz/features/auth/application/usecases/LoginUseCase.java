package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.LoginRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.AuthService;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class LoginUseCase implements UseCase<LoginRequest, AuthResponse> {
    private final AuthService authService;
    private final AuthMapper authMapper;
    private final TokenService tokenService;
    private final UserValidationService userValidationService;
    
    public LoginUseCase(
            AuthService authService, AuthMapper authMapper, TokenService tokenService,
            UserValidationService userValidationService
    ) {
        this.authService = authService;
        this.authMapper = authMapper;
        this.tokenService = tokenService;
        this.userValidationService = userValidationService;
    }
    
    
    @Override
    public AuthResponse execute(LoginRequest request) {
        AppUser authenticatedUser = authService.authenticate(request.email(), request.password());
        TokenPair tokens = tokenService.createUserTokens(authenticatedUser.getEmail());
        return authMapper.toResponse(tokens, authenticatedUser);
    }
}
