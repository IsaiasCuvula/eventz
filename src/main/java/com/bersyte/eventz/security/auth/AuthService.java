package com.bersyte.eventz.security.auth;

import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.security.jwt.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse createUser(RegisterRequestDTO requestDTO) {
        AuthUser user = AuthMapper.toAuthUser(requestDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            authRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(e.getMessage());
        }
        final String token = jwtService.generateToken(user.getUsername());
        return AuthMapper.toAuthResponse(user, token);
    }


    public String login(LoginRequestDTO requestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDTO.email(), requestDTO.password()
                    )
            );
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authentication.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return "Authentication failed";
    }
}
