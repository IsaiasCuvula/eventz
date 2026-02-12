package com.bersyte.eventz.features.auth.presentation.controllers;

import com.bersyte.eventz.features.auth.application.dtos.*;
import com.bersyte.eventz.features.auth.application.usecases.*;
import com.bersyte.eventz.features.users.application.dtos.ForgotPasswordRequest;
import com.bersyte.eventz.features.users.application.dtos.LogoutRequest;
import com.bersyte.eventz.features.users.application.dtos.RefreshTokenRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final ResendVerificationCodeUseCase resendVerificationCodeUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ResetPasswordConfirmUseCase resetPasswordConfirmUseCase;
    

    public AuthController(
            SignUpUseCase signUpUseCase, LoginUseCase loginUseCase,
            LogoutUseCase logoutUseCase, RefreshTokenUseCase refreshTokenUseCase,
            VerifyEmailUseCase verifyEmailUseCase,
            ResetPasswordConfirmUseCase resetPasswordConfirmUseCase,
            ResendVerificationCodeUseCase resendVerificationCodeUseCase,
            ResetPasswordUseCase resetPasswordUseCase
    ) {
        this.signUpUseCase = signUpUseCase;
        this.loginUseCase = loginUseCase;
        this.logoutUseCase = logoutUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.verifyEmailUseCase = verifyEmailUseCase;
        this.resetPasswordConfirmUseCase = resetPasswordConfirmUseCase;
        this.resendVerificationCodeUseCase = resendVerificationCodeUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = signUpUseCase.execute (request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUseCase.execute (request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(
            @RequestBody VerificationRequest request
    ) {
        verifyEmailUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/resend-code/{userId}")
    public ResponseEntity<?> resend(@PathVariable String userId) {
        resendVerificationCodeUseCase.execute(userId);
        return ResponseEntity.noContent().build();
    }
    
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request){
        logoutUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        resetPasswordUseCase.execute(request);
        return ResponseEntity.accepted().build();
    }
    
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<?> resetPassword(@RequestBody ConfirmPasswordRequest request){
        resetPasswordConfirmUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
    
}
