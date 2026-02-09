package com.bersyte.eventz.features.auth;

import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.VerificationRequest;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import com.bersyte.eventz.features.email.EmailService;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.common.security.JwtService;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserEntityMapper userMapper;

    public AuthService(UserJpaRepository userJpaRepository, PasswordEncoder encoder, JwtService jwtService, AuthenticationManager authenticationManager, EmailService emailService, UserEntityMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }
    
   

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return;
            }

            final String refreshToken = authHeader.substring(7);
            final String email = jwtService.extractUsername(refreshToken);
            final UserEntity user = findAuthUserByEmail(email);

            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(email);

                //revokeAllUserToken(user)
                //saveUserToken(user, accessToken)
                AuthResponse authResponse = new AuthResponse(
                        accessToken,
                        refreshToken,
                        jwtService.extractExpiration(accessToken)
                );
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }

        } catch (AuthException e) {
            throw new AuthException(e.getMessage());
        }
    }


    public void resendVerificationCode(String email) {
        try {
            UserEntity user = findAuthUserByEmail(email);

            if (user.isEnabled()) {
                throw new AuthException("User already verified");
            }

            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiration(
                    LocalDateTime.now().plusMinutes(15)
            );
            sendVerificationEmail(user);
            userJpaRepository.save(user);
        } catch (Exception e) {
            throw new AuthException(
                    "Failed to resend verification email  " + e.getLocalizedMessage()
            );
        }

    }

}
