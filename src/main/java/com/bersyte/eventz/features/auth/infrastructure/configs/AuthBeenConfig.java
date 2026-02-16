package com.bersyte.eventz.features.auth.infrastructure.configs;

import com.bersyte.eventz.common.domain.services.IdGenerator;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.application.usecases.*;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.auth.domain.service.*;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AuthBeenConfig {
    
    @Bean
    public AuthMapper authMapper(){
        return new AuthMapper();
    }
    
    @Bean
    public SignUpUseCase signUpUseCase(
            UserRepository userRepository,
            PasswordHasher passwordEncoder, CodeGenerator codeGenerator,
            IdGenerator idGenerator, AuthMapper authMapper,
            AuthProperties authSettings, Clock clock,AuthEventPublisher authEventPublisher
    ){
        return new SignUpUseCase(
                userRepository, passwordEncoder, codeGenerator,
                idGenerator, authMapper, authSettings, clock,authEventPublisher
        );
    }
    @Bean
    public LoginUseCase loginUseCase(
            AuthService authService, AuthMapper authMapper,
            TokenService tokenService
    ){
        return new LoginUseCase(
                authService, authMapper, tokenService);
    }
    
    @Bean
    public LogoutUseCase logoutUseCase(
            RefreshTokenRepository refreshTokenRepository,
            TokenService tokenService
    ) {
        return new LogoutUseCase(refreshTokenRepository, tokenService);
    }
    
    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(
            TokenService tokenService,
             UserValidationService userValidationService, AuthMapper authMapper
    ){
        return new RefreshTokenUseCase(tokenService, userValidationService, authMapper);
    }
    
    @Bean
    public VerifyEmailUseCase verifyEmailUseCase(
            UserValidationService userValidationService,
            UserRepository userRepository, TokenService tokenService,
            AuthMapper mapper, Clock clock,AuthEventPublisher authEventPublisher
    ){
        return new VerifyEmailUseCase(
                userValidationService, userRepository,
                tokenService, mapper, clock,authEventPublisher
        );
    }
    
    @Bean
    public ResetPasswordConfirmUseCase confirmPasswordResetUseCase(
            UserValidationService userValidationService,
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            Clock clock,AuthEventPublisher authEventPublisher
    ) {
        return new ResetPasswordConfirmUseCase(
                userValidationService, userRepository,
                passwordHasher, clock,authEventPublisher
        );
    }
    
    @Bean
    public ResendVerificationCodeUseCase resendVerificationCodeUseCase(
            UserValidationService userValidationService,
            UserRepository userRepository, CodeGenerator codeGenerator,
            AuthProperties authSettings, Clock clock,
            AuthEventPublisher authEventPublisher
    ){
        return new ResendVerificationCodeUseCase(
                userValidationService, userRepository,
                codeGenerator, authSettings, clock,
                authEventPublisher
        );
    }
    
    @Bean
    public ResetPasswordRequestUseCase resetPasswordUseCase(
            UserValidationService userValidationService,
            CodeGenerator codeGenerator, UserRepository userRepository,
            AuthProperties authSettings, Clock clock,
            AuthEventPublisher authEventPublisher
    ){
        return new ResetPasswordRequestUseCase(
                userValidationService, codeGenerator, userRepository,
                authSettings, clock, authEventPublisher
        );
    }
}
