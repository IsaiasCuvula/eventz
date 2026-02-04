package com.bersyte.eventz.features.auth.presentation.controllers;

import com.bersyte.eventz.features.auth.*;
import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.application.dtos.LoginRequest;
import com.bersyte.eventz.features.auth.application.dtos.SignupRequest;
import com.bersyte.eventz.features.auth.application.dtos.VerifyUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest requestDTO) {
        AuthResponse response = authService.signup (requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest requestDTO) {
        AuthResponse response = authService.login (requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(
            @RequestBody VerifyUserRequest verifyUserDto
    ) {
        authService.verifyUser(verifyUserDto);
        return ResponseEntity.ok("User verified successfully");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody String email) {
        try {
            authService.resendVerificationCode(email);
            return ResponseEntity.ok("Code verification resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}
