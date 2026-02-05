package com.bersyte.eventz.features.auth.infrastructure.security;

import com.bersyte.eventz.common.domain.exceptions.ResourceNotFoundException;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidCredentialsException;
import com.bersyte.eventz.features.auth.domain.service.AuthService;
import com.bersyte.eventz.features.auth.infrastructure.persistence.AppUserPrincipal;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceAdapter implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserEntityMapper userEntityMapper;
    
    public AuthServiceAdapter(AuthenticationManager authenticationManager, UserEntityMapper userEntityMapper) {
        this.authenticationManager = authenticationManager;
        this.userEntityMapper = userEntityMapper;
    }
    
    @Override
    public AppUser authenticate(String email, String password) {
       try{
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(email, password)
           );
           AppUserPrincipal userPrincipal= (AppUserPrincipal)authentication.getPrincipal();
           UserEntity userEntity = userPrincipal.getUserEntity();
           return userEntityMapper.toDomain(userEntity);
       } catch (BadCredentialsException e) {
           throw new InvalidCredentialsException();
       } catch (UsernameNotFoundException e) {
           throw new ResourceNotFoundException("User", email);
       } catch (Exception e) {
           throw new AuthException("Unexpected error during authentication");
       }
    }
}
