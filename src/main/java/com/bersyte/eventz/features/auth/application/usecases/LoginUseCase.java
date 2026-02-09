package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.LoginRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.AuthService;
import com.bersyte.eventz.features.auth.domain.service.TokenService;

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
        AuthUser authenticatedUser = authService.authenticate(request.email(), request.password());
        if(!authenticatedUser.isVerified()){
            //the tokens will be null meaning the user do not have permission
            // to access the system data if not verified
            return authMapper.toUserNotVerifiedResponse(authenticatedUser);
        }
        TokenPair tokens = tokenService.createUserTokens(authenticatedUser.id());
        return authMapper.toResponse(tokens, authenticatedUser);
    }
}
