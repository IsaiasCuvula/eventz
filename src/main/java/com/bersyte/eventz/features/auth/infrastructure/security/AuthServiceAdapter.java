package com.bersyte.eventz.features.auth.infrastructure.security;

import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidCredentialsException;
import com.bersyte.eventz.features.auth.domain.model.AuthUser;
import com.bersyte.eventz.features.auth.domain.service.AuthService;
import com.bersyte.eventz.features.auth.infrastructure.persistence.dtos.AppUserPrincipal;
import com.bersyte.eventz.features.users.domain.exception.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceAdapter implements AuthService {
    private final AuthenticationManager authenticationManager;
    
    public AuthServiceAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public AuthUser authenticate(String email, String password) {
       try{
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(email, password)
           );
           AppUserPrincipal userPrincipal= (AppUserPrincipal)authentication.getPrincipal();
           return new AuthUser(
                   userPrincipal.id(),
                   userPrincipal.email(),
                   userPrincipal.firstName(),
                   userPrincipal.lastName(),
                   userPrincipal.phone(),
                   userPrincipal.role(),
                   userPrincipal.isEnabled(),
                   userPrincipal.verified(),
                   userPrincipal.createdAt()
           );
       } catch (BadCredentialsException e) {
           throw new InvalidCredentialsException();
       } catch (UsernameNotFoundException e) {
           throw new UserNotFoundException("User with email " + email + " not found");
       } catch (Exception e) {
           throw new AuthException("Unexpected error during authentication");
       }
    }
}
