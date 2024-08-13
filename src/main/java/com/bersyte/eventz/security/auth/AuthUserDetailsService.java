package com.bersyte.eventz.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    public AuthUserDetailsService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser authUser = authRepository.findByEmail(username);
        if (authUser == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return new AppUserPrincipal(authUser);
    }
}
