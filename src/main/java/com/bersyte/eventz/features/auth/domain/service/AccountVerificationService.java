package com.bersyte.eventz.features.auth.domain.service;

public interface AccountVerificationService {
    void resendCode(String email);
}
