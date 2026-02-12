package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.features.auth.application.dtos.ConfirmPasswordRequest;
import com.bersyte.eventz.features.auth.infrastructure.security.BCryptPasswordHasherAdapter;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.time.Clock;
import java.time.LocalDateTime;

public class ResetPasswordConfirmUseCase implements VoidUseCase<ConfirmPasswordRequest> {
    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final BCryptPasswordHasherAdapter passwordHasher;
    private final Clock clock;
    
    public ResetPasswordConfirmUseCase(UserValidationService userValidationService, UserRepository userRepository, BCryptPasswordHasherAdapter passwordHasher, Clock clock) {
        this.userValidationService = userValidationService;
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
    }
    
    @Override
    public void execute(ConfirmPasswordRequest request) {
        AppUser targetUser = userValidationService.getValidUserByEmail(request.email());
        String newPasswordEncoded = passwordHasher.encode(request.newPassword());
        LocalDateTime updatedAt = LocalDateTime.now(clock);
        AppUser updatedUser = targetUser.changePassword(
                request.recoveryCode(), newPasswordEncoded,updatedAt
        );
        AppUser savedUser = userRepository.update(updatedUser);
        
        //send email to the user notifying that the password was changed successfully
    }
}
