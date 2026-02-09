package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.VerificationRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.auth.domain.service.TokenService;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.time.Clock;
import java.time.LocalDateTime;

public class VerifyEmailUseCase implements UseCase<VerificationRequest, AuthResponse> {
    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthMapper mapper;
    private final Clock clock;
    
    public VerifyEmailUseCase(
            UserValidationService userValidationService,
            UserRepository userRepository, TokenService tokenService, AuthMapper mapper, Clock clock
    ) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.mapper = mapper;
        this.clock = clock;
    }
    
    @Override
    public AuthResponse execute(VerificationRequest request) {
        AppUser requester = userValidationService.getValidUserById(request.requesterId());
        String verificationCode = request.verificationCode();
        LocalDateTime verificationTime = LocalDateTime.now(clock);
        if(requester.isVerificationCodeExpired(verificationTime)){
            throw new AuthException("Verification code expired");
        }
        AppUser verifiedUser = requester.verifyCode(verificationCode, verificationTime);
        AppUser updatedUser = userRepository.update(verifiedUser);
        //Sending email to the user updatedUser.getEmail();
        TokenPair tokens = tokenService.createUserTokens(updatedUser.getId());
        AuthUser authenticatedUser = mapper.fromAppUser(updatedUser);
        return mapper.toResponse(tokens, authenticatedUser);
    }
}
