package com.bersyte.eventz.features.auth.domain.exceptions;

public class InvalidVerificationCodeException  extends RuntimeException {
    
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}