package com.bersyte.eventz.features.auth.domain.repository;

import com.bersyte.eventz.features.users.domain.model.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthRepository {
    String generateVerificationCode();
    void resendVerificationCode(String email);
    AppUser authenticate(String email, String password);
    void verifyUser(String email,String verificationCode);
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
