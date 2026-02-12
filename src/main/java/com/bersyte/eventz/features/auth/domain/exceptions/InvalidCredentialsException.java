package com.bersyte.eventz.features.auth.domain.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
            super("Invalid email or password.");
    }
}
