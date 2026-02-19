package com.bersyte.eventz.features.users.domain.services;

import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.users.domain.exception.UserNotFoundException;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;

import java.util.UUID;

public class UserValidationService {
    private final UserRepository repository;
    
    public UserValidationService(UserRepository repository) {
        this.repository = repository;
    }
    
    
    public AppUser getRequesterById(UUID id){
        return this.getValidUserById(id);
    }
    
    public AppUser getAuthorizedOrganizerById(UUID id) {
        AppUser user = this.getValidUserById(id);
        if (!user.canManageEvents()) {
            throw new UnauthorizedException("User cannot organize events");
        }
        return user;
    }
    
    
    public AppUser getValidUserById(UUID userId){
        return repository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("User with id: " + userId + " not found")
        );
    }
    
    public AppUser getValidUserByEmail(String email){
        return repository.findByEmail(email).orElseThrow(
                ()-> new UserNotFoundException("User with email " + email + " not found")
        );
    }
    
}
