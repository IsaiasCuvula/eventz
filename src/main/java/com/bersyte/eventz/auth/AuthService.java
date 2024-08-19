package com.bersyte.eventz.auth;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserMapper;
import com.bersyte.eventz.common.UserResponseDTO;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.security.JWTService;
import com.bersyte.eventz.users.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponseDTO createUser(RegisterRequestDto requestDTO) {
        AppUser user = UserMapper.toUserEntity(requestDTO);
        user.setPassword(encoder.encode(user.getPassword()));

        Optional<AppUser> userOptional = findAuthUserByEmail(requestDTO.email());
        if (userOptional.isPresent()) {
            throw new DatabaseOperationException("User already exists");
        }
        //
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(e.getLocalizedMessage());
        }
        //final String token = jwtService.generateToken(user.getEmail());
        return UserMapper.toUserResponseDTO(user);
    }

    public LoginResponse login(LoginRequestDto requestDTO) {
        try {
            String email = requestDTO.email();
            String password = requestDTO.password();
            AppUser user = authenticate(email, password);

            String token = jwtService.generateToken(user.getEmail());
            return new LoginResponse(
                    token, jwtService.getExpirationTime()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private AppUser authenticate(String email, String password) {
        try {
            AppUser user = findAuthUserByEmail(email).orElseThrow(
                    () -> new DatabaseOperationException("User does not exists")
            );
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email, password
                    )
            );
            if (!authentication.isAuthenticated()) {
                throw new RuntimeException("Authentication failed");
            }

            return user;
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(e.getLocalizedMessage());
        }
    }


    private Optional<AppUser> findAuthUserByEmail(String email) {
        try {
            return Optional.ofNullable(userRepository.findByEmail(email));
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get user by email %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }
}
