package com.bersyte.eventz.features.auth.application.mappers;

import com.bersyte.eventz.features.auth.application.dtos.AuthResponse;
import com.bersyte.eventz.security.UserTokens;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;


public class AuthMapper {
    
    private final UserMapper userMapper;
    
    public AuthMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    public AuthResponse toResponse(UserTokens tokens, AppUser user) {
          return new AuthResponse(
                  tokens.token(), tokens.refreshToken(),
                  tokens.expiration(), userMapper.toResponse(user)
          );
    }
}
