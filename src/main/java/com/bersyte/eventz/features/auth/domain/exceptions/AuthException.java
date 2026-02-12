package com.bersyte.eventz.features.auth.domain.exceptions;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}