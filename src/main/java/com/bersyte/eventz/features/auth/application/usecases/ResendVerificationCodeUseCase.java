package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.features.auth.application.events.VerificationCodeResentEvent;
import com.bersyte.eventz.features.auth.domain.service.*;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public class ResendVerificationCodeUseCase implements VoidUseCase<String> {
    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final CodeGenerator codeGenerator;
    private final AuthProperties authSettings;
    private final Clock clock;
    private final AuthEventPublisher authEventPublisher;
    
    public ResendVerificationCodeUseCase(
            UserValidationService userValidationService, UserRepository userRepository,
            CodeGenerator codeGenerator, AuthProperties authSettings, Clock clock, AuthEventPublisher authEventPublisher
    ) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.codeGenerator = codeGenerator;
        this.authSettings = authSettings;
        this.clock = clock;
        this.authEventPublisher = authEventPublisher;
    }
    
    @Transactional
    @Override
    public void execute(String userId) {
        AppUser targetUser = userValidationService.getRequesterById(userId);
        LocalDateTime now = LocalDateTime.now(clock);
        String verificationCode = codeGenerator.generate();
        Duration expirationTime =  authSettings.getVerificationCodeExpiration();
        LocalDateTime verificationExpiration = now.plus(expirationTime);
        AppUser updatedUser = targetUser.updateVerificationCode(now, verificationCode, verificationExpiration);
        AppUser savedUser = userRepository.update(updatedUser);
        
        //Send new verification code email
        authEventPublisher.publishSendVerificationCode(
                new VerificationCodeResentEvent(
                        savedUser.getEmail(),
                        savedUser.getFullName(),
                        savedUser.getVerificationCode()
                )
        );
    }
}
