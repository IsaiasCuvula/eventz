package com.bersyte.eventz.features.auth.domain.service;

import com.bersyte.eventz.features.users.domain.model.AppUser;


public interface AuthService {
    AppUser authenticate(String email, String password);
//  String resetPassword();
//  boolean blockAccount();
}
