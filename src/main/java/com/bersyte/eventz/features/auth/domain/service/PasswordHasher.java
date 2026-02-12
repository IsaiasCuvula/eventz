package com.bersyte.eventz.features.auth.domain.service;

public interface PasswordHasher {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
