package com.bersyte.eventz.features.users.domain.services;

import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;

public class UserValidationService {
    private final UserRepository repository;
    
    public UserValidationService(UserRepository repository) {
        this.repository = repository;
    }
    
    
    public AppUser getRequester(String email){
        return this.getValidUserByEmail(email);
    }
    
    public AppUser getAuthorizedOrganizer(String email) {
        AppUser user = getValidUserByEmail(email);
        if (!user.canManageEvents()) {
            throw new UnauthorizedException("User cannot organize events");
        }
        return user;
    }
    
    
    public AppUser getValidUserById(String userId){
        return repository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User", userId)
        );
    }
    
    public AppUser getValidUserByEmail(String email){
        return repository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User", email)
        );
    }
    
}
