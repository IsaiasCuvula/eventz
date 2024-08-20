package com.bersyte.eventz.auth;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository authRepository;

    public AuthUserDetailsService(UserRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> authUser = authRepository.findByEmail(username);
        if (authUser.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return new AppUserPrincipal(authUser.get());
    }
}
