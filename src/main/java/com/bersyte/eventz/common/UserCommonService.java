package com.bersyte.eventz.common;

import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommonService {

    final private UserRepository userRepository;


    public AppUser getUserById(Long id) {
        try {
            Optional<AppUser> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                throw new DatabaseOperationException("User not found");
            }
            return userOptional.get();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get user by email %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public AppUser findUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new DatabaseOperationException("User not found")
            );
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get user by email %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }
}
