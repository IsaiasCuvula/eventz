package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.SignupRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.service.AuthProperties;
import com.bersyte.eventz.features.auth.domain.service.CodeGenerator;
import com.bersyte.eventz.features.auth.domain.service.PasswordHasher;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

public class SignUpUseCase implements UseCase<SignupRequest, AuthResponse> {
    private final UserRepository userRepository;
    private final PasswordHasher passwordEncoder;
    private final CodeGenerator codeGenerator;
    private final IdGenerator idGenerator;
    private final AuthMapper authMapper;
    private final AuthProperties authSettings;
    private final Clock clock;
    
    public SignUpUseCase(
            UserRepository userRepository,
            PasswordHasher passwordEncoder, CodeGenerator codeGenerator,
            IdGenerator idGenerator, AuthMapper authMapper,
            AuthProperties authSettings, Clock clock
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.codeGenerator = codeGenerator;
        this.idGenerator = idGenerator;
        this.authMapper = authMapper;
        this.authSettings = authSettings;
        this.clock = clock;
    }
    
    @Transactional
    @Override
    public AuthResponse execute(SignupRequest request) {
        boolean alreadyExists = userRepository.alreadyExistsByEmail(request.email());
        
        if(alreadyExists){
            throw new AuthException("Email already in use");
        }
       
        String userId = idGenerator.generateUuid();
        LocalDateTime now = LocalDateTime.now(clock);
        String encodedPassword = passwordEncoder.encode(request.password());
        String verificationCode = codeGenerator.generate();
        Duration expirationTime =  authSettings.getVerificationCodeExpiration();
        
        AppUser newUser = AppUser.create(userId,
                request.email(), request.firstName(), request.lastName(),
                request.phone(), now,verificationCode,encodedPassword,
                now.plusMinutes(expirationTime.toMinutes())
        );
        AppUser savedUser = userRepository.save(newUser);
        //emailService.sendVerificationEmail(savedUser); (events)
       //User only has access tokens after verifying the email.
        return authMapper.toSignUpResponse(savedUser);
    }
}
