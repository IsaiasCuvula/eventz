package com.bersyte.eventz.common;

import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.users.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
public class UserCommonService {

    final private UserRepository userRepository;

    public UserCommonService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public AppUser getUserById(Long id) {
        try {
            return userRepository.findById (id).orElseThrow (
                    () -> new DatabaseOperationException ("User not found")
            );
        } catch (DataAccessException e) {
            throw new DatabaseOperationException (
                    "Database error occurred " + e.getLocalizedMessage ()
            );
        }
    }

    public AppUser getUserByEmail(String email) {
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
