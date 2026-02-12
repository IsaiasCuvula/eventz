package com.bersyte.eventz.features.auth.domain.service;

import com.bersyte.eventz.features.auth.domain.model.AuthUser;


public interface AuthService {
    AuthUser authenticate(String email, String password);
//  String resetPassword();
//  boolean blockAccount();
}
