package com.bersyte.eventz.auth;

import com.bersyte.eventz.common.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody RegisterRequestDto requestDTO) {
        UserResponseDto response = authService.signup(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto requestDTO) {
        LoginResponse response = authService.login(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(
            @RequestBody VerifyUserDto verifyUserDto
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
}
