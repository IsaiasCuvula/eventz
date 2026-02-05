package com.bersyte.eventz.features.auth.domain.service;

public interface AccountVerificationService {
    boolean isVerificationCodeValid(String code, String storedCode);
    void verify(String email, String code);
    void resendCode(String email);
}
