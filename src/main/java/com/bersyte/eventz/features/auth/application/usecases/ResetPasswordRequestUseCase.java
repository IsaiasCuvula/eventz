package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.features.auth.application.events.PasswordResetRequestedEvent;
import com.bersyte.eventz.features.auth.domain.service.AuthEventPublisher;
import com.bersyte.eventz.features.auth.domain.service.AuthProperties;
import com.bersyte.eventz.features.auth.domain.service.CodeGenerator;
import com.bersyte.eventz.features.users.application.dtos.ForgotPasswordRequest;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public class ResetPasswordRequestUseCase implements VoidUseCase<ForgotPasswordRequest> {
    private final UserValidationService userValidationService;
    private final CodeGenerator codeGenerator;
    private final UserRepository userRepository;
    private final AuthProperties authSettings;
    private final Clock clock;
    private final AuthEventPublisher authEventPublisher;
    
    public ResetPasswordRequestUseCase(
            UserValidationService userValidationService,
            CodeGenerator codeGenerator, UserRepository userRepository,
            AuthProperties authSettings, Clock clock, AuthEventPublisher authEventPublisher
    ) {
        this.userValidationService = userValidationService;
        this.codeGenerator = codeGenerator;
        this.userRepository = userRepository;
        this.authSettings = authSettings;
        this.clock = clock;
        this.authEventPublisher = authEventPublisher;
    }
    
    @Transactional
    @Override
    public void execute(ForgotPasswordRequest request) {
        AppUser targetUser = userValidationService.getValidUserByEmail(request.email());
        String recoveryCode = codeGenerator.generate();
        Duration validity = authSettings.getVerificationCodeExpiration();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        AppUser updatedUser = targetUser.requestRecoveryCode(
                recoveryCode,createdAt, validity
        );
        AppUser savedUser = userRepository.update(updatedUser);
        
        //Send email with the code
        authEventPublisher.publishUserResetPassword(
                new PasswordResetRequestedEvent(
                        savedUser.getEmail(), savedUser.getFullName(),
                        recoveryCode
                )
        );
    }
}
