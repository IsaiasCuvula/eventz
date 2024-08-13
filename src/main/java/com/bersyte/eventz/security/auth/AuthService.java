package com.bersyte.eventz.security.auth;

import com.bersyte.eventz.exceptions.DatabaseOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    private final PasswordEncoder encoder;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        AppUser user = UserMapper.toUserEntity(userRequestDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            authRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(e.getMessage());
        }
        return UserMapper.toUserResponseDTO(user);
    }


}
