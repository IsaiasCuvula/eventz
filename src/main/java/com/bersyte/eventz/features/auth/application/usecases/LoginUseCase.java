package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.LoginRequest;

public class LoginUseCase implements UseCase<LoginRequest, AuthResponse> {
    
    
    @Override
    public AuthResponse execute(LoginRequest input) {
        return null;
    }
}
