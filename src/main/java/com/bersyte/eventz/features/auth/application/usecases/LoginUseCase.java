package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.LoginRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.AuthService;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import jakarta.transaction.Transactional;


public class LoginUseCase implements UseCase<LoginRequest, AuthResponse> {
    private final AuthService authService;
    private final AuthMapper authMapper;
    private final TokenService tokenService;
    
    public LoginUseCase(
            AuthService authService, AuthMapper authMapper,
            TokenService tokenService
    ) {
        this.authService = authService;
        this.authMapper = authMapper;
        this.tokenService = tokenService;
    }
    
    @Transactional
    @Override
    public AuthResponse execute(LoginRequest request) {
        AuthUser authenticatedUser = authService.authenticate(request.email(), request.password());
        if(!authenticatedUser.isVerified()){
            //the tokens will be null meaning the user do not have permission
            // to access the system data if not verified
            return authMapper.toUserNotVerifiedResponse(authenticatedUser);
        }
        
        //In case we want to restrict the numbers of devices logged in with the same account
        //Before saving the new token, count how many tokens the userId already has.
        //If it's greater than 4 (devices), you prevent login or delete the oldest one.
        TokenPair tokens = tokenService.createAndPersistTokens(authenticatedUser);
        return authMapper.toResponse(tokens, authenticatedUser);
    }
}
