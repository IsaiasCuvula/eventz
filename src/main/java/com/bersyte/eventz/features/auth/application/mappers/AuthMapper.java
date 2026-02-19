package com.bersyte.eventz.features.auth.application.mappers;

import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.model.TokenPair;
import com.bersyte.eventz.features.users.domain.model.AppUser;



public class AuthMapper {
    
    
    
    public AuthResponse toSignUpResponse(AppUser appUser) {
        AuthUser authUser = this.fromAppUser(appUser);
        return new AuthResponse(
                null, null,
                null,
                authUser.userId(),
                authUser.email(),
                authUser.firstName(),
                authUser.lastName(),
                authUser.role(),
                authUser.isEnabled(),
                authUser.isVerified(),
                authUser.createdAt()
        );
    }
    
    public AuthResponse toUserNotVerifiedResponse(AuthUser authUser) {
        return new AuthResponse(
                null, null,
                null,
                authUser.userId(),
                authUser.email(),
                authUser.firstName(),
                authUser.lastName(),
                authUser.role(),
                authUser.isEnabled(),
                authUser.isVerified(),
                authUser.createdAt()
        );
    }
    
    
    public AuthResponse toResponse(TokenPair tokens, AuthUser user) {
          return new AuthResponse(
                  tokens.token(), tokens.refreshToken(),
                  tokens.expiration(),
                  user.userId(),
                  user.email(),
                  user.firstName(),
                  user.lastName(),
                  user.role(),
                  user.isEnabled(),
                  user.isVerified(),
                  user.createdAt()
          );
    }
    
    public AuthUser fromAppUser(AppUser appUser){
        return new AuthUser(
                appUser.getId(),
                appUser.getEmail(),
                appUser.getPassword(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getRole(),
                appUser.isEnabled(),
                appUser.isVerified(),
                appUser.getCreatedAt()
        );
    }
    
}
