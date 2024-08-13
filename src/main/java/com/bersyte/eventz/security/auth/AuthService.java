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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponseDTO createUser(RegisterRequestDTO requestDTO) {
        AppUser user = UserMapper.toUserEntity(requestDTO);
        user.setPassword(encoder.encode(user.getPassword()));

        Optional<AppUser> userOptional = findAuthUserByEmail(requestDTO.email());
        if (userOptional.isPresent()) {
            throw new DatabaseOperationException("User already exists");
        }
        //
        try {
            authRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(e.getLocalizedMessage());
        }
        final String token = jwtService.generateToken(user.getEmail());
        return UserMapper.toUserResponseDTO(user, token);
    }


    public String login(LoginRequestDTO requestDTO) {
        Optional<AppUser> user = findAuthUserByEmail(requestDTO.email());
        if (user.isPresent()) {
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
        } else {
            throw new DatabaseOperationException("User does not exists");
        }
        return "Authentication failed";
    }

    private Optional<AppUser> findAuthUserByEmail(String email) {
        try {
            return Optional.ofNullable(authRepository.findByEmail(email));
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get user by email %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }
}
