package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.features.auth.application.dtos.VerificationRequest;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.time.Clock;
import java.time.LocalDateTime;

public class VerifyEmailUseCase implements VoidUseCase<VerificationRequest> {
    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final Clock clock;
    
    public VerifyEmailUseCase(
            UserValidationService userValidationService,
            UserRepository userRepository, Clock clock
    ) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.clock = clock;
    }
    
    @Override
    public void execute(VerificationRequest request) {
        AppUser requester = userValidationService.getValidUserById(request.requesterId());
        String verificationCode = request.verificationCode();
        LocalDateTime verificationTime = LocalDateTime.now(clock);
        if(requester.isVerificationCodeExpired(verificationTime)){
            throw new AuthException("Verification code expired");
        }
        AppUser verifiedUser = requester.verifyCode(verificationCode, verificationTime);
        AppUser updatedUser = userRepository.update(verifiedUser);
        //Sending email to the user updatedUser.getEmail();
    }
}
