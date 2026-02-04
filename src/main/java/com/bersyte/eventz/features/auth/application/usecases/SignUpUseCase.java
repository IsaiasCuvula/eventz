package com.bersyte.eventz.features.auth.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.common.presentation.exceptions.AuthException;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.SignupRequest;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.domain.repository.AuthRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.security.JWTService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.LocalDateTime;

public class SignUpUseCase implements UseCase<SignupRequest, AuthResponse> {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final IdGenerator idGenerator;
    private final AuthMapper authMapper;
    private final Clock clock;
    
    public SignUpUseCase(
            UserRepository userRepository,
            AuthRepository authRepository, PasswordEncoder passwordEncoder,
            JWTService jwtService, IdGenerator idGenerator, AuthMapper authMapper, Clock clock
    ) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.idGenerator = idGenerator;
        this.authMapper = authMapper;
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
        String verificationCode = authRepository.generateVerificationCode();
        
        AppUser newUser = AppUser.create( userId,
                request.email(), request.firstName(), request.lastName(),
                request.phone(), now,verificationCode,encodedPassword, now.plusMinutes(15)
        );
        AppUser savedUser = userRepository.save(newUser);
        
        //emailService.sendVerificationEmail(savedUser);
        
        return authMapper.toResponse(
                jwtService.createUserTokens(savedUser.getEmail()),
                savedUser
        );
    }
}
