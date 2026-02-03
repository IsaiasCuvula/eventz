package com.bersyte.eventz.features.auth;

import com.bersyte.eventz.features.auth.application.dtos.AuthResponseDto;
import com.bersyte.eventz.features.auth.application.dtos.LoginDto;
import com.bersyte.eventz.features.auth.application.dtos.RegisterDto;
import com.bersyte.eventz.features.auth.application.dtos.VerifyUserDto;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import com.bersyte.eventz.features.email.EmailService;
import com.bersyte.eventz.common.presentation.exceptions.AuthException;
import com.bersyte.eventz.security.JWTService;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserEntityMapper userMapper;

    public AuthService(UserJpaRepository userJpaRepository, PasswordEncoder encoder, JWTService jwtService, AuthenticationManager authenticationManager, EmailService emailService, UserEntityMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    public AuthResponseDto signup(RegisterDto requestDTO) {
        try {
            UserEntity newUser = userMapper.toUserEntity (requestDTO);
            newUser.setPassword(encoder.encode(newUser.getPassword()));

            if (hasUser(requestDTO.email())) {
                throw new AuthException("User already exists");
            }
            newUser.setVerificationCode(generateVerificationCode());
            newUser.setVerificationExpiration(
                    LocalDateTime.now().plusMinutes(15)
            );
            newUser.setEnabled(false);
            final UserEntity savedUser = userJpaRepository.save(newUser);
            sendVerificationEmail(savedUser);
            return getAuthResponse(savedUser);
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }

    public AuthResponseDto login(LoginDto requestDTO) {
        try {
            String email = requestDTO.email();
            String password = requestDTO.password();
            UserEntity user = authenticate(email, password);

            return getAuthResponse(user);
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }

    private AuthResponseDto getAuthResponse(UserEntity user) {
        String token = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        Date expiration = jwtService.extractExpiration(token);
        //saveUserToken(user, token)
        return new AuthResponseDto (token, refreshToken, expiration);
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
                AuthResponseDto authResponse = new AuthResponseDto (
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

//    private void revokeAllUserTokens(UserEntity user) {
//
//    }

    public void verifyUser(VerifyUserDto data) {
        try {
            UserEntity user = findAuthUserByEmail(data.getEmail());

            if (user.isEnabled()) {
                throw new AuthException("User already verified");
            }

            if (user.getVerificationExpiration().isBefore(LocalDateTime.now())) {
                throw new AuthException("Verification code has expired");
            }

            if (!user.getVerificationCode().equals(data.getVerificationCode())) {
                throw new AuthException("Invalid verification code");
            }

            user.setVerificationExpiration(null);
            user.setVerificationCode(null);
            user.setEnabled(true);
            userJpaRepository.save(user);
        } catch (Exception e) {
            throw new AuthException(
                    "Something went wrong while verifying user - " + e.getLocalizedMessage()
            );
        }


    }

    private UserEntity authenticate(String email, String password) {
        try {
            UserEntity user = findAuthUserByEmail(email);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, password
                    )
            );
            if (!authentication.isAuthenticated()) {
                throw new AuthException("Authentication failed");
            }

            return user;
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }


    private UserEntity findAuthUserByEmail(String email) {
        try {
            final Optional<UserEntity> userOptional = userJpaRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new AuthException("User not found");
            }
            return userOptional.get();
        } catch (Exception e) {
            throw new AuthException(
                    "Failed to find auth user - " + e.getMessage()
            );
        }
    }

    private boolean hasUser(String email) {
        try {
            final Optional<UserEntity> userOptional = userJpaRepository.findByEmail(email);
            return userOptional.isPresent();
        } catch (Exception e) {
            throw new AuthException(
                    "Failed to get user by email- " + e.getMessage()
            );
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


    private void sendVerificationEmail(UserEntity user) {
        String subject = "Eventz Email Verification Code";
        String verificationCode = user.getVerificationCode();
        String htmlBody = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(
                    user.getEmail(), subject, htmlBody
            );
        } catch (MessagingException e) {
            throw new AuthException(e.getLocalizedMessage());
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
