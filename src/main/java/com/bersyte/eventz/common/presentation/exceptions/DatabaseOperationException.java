package com.bersyte.eventz.common.presentation.exceptions;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super(message);
    }
}