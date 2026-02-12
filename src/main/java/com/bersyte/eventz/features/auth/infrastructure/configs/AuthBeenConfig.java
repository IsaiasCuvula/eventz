package com.bersyte.eventz.features.auth.infrastructure.configs;

import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.auth.application.mappers.AuthMapper;
import com.bersyte.eventz.features.auth.application.usecases.*;
import com.bersyte.eventz.features.auth.domain.repository.RefreshTokenRepository;
import com.bersyte.eventz.features.auth.domain.service.*;
import com.bersyte.eventz.features.auth.infrastructure.security.BCryptPasswordHasherAdapter;
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
            AuthProperties authSettings, Clock clock
    ){
        return new SignUpUseCase(
                userRepository, passwordEncoder, codeGenerator, idGenerator, authMapper, authSettings, clock);
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
            RefreshTokenRepository refreshTokenRepository, TokenService tokenService
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
            UserRepository userRepository, TokenService tokenService, AuthMapper mapper, Clock clock
    ){
        return new VerifyEmailUseCase(userValidationService, userRepository, tokenService, mapper, clock);
    }
    
    @Bean
    public ResetPasswordConfirmUseCase confirmPasswordResetUseCase(
            UserValidationService userValidationService,
            UserRepository userRepository,
            BCryptPasswordHasherAdapter passwordHasher, Clock clock
    ) {
        return new ResetPasswordConfirmUseCase(
                userValidationService, userRepository, passwordHasher, clock);
    }
    
    @Bean
    public ResendVerificationCodeUseCase resendVerificationCodeUseCase(
            UserValidationService userValidationService, UserRepository userRepository,
            CodeGenerator codeGenerator, AuthProperties authSettings, Clock clock
    ){
        return new ResendVerificationCodeUseCase(
                userValidationService, userRepository, codeGenerator, authSettings, clock);
    }
    
    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            UserValidationService userValidationService,
            CodeGenerator codeGenerator, UserRepository userRepository,
            AuthProperties authSettings, Clock clock
    ){
        return new ResetPasswordUseCase(
                userValidationService, codeGenerator, userRepository, authSettings, clock);
    }
}
