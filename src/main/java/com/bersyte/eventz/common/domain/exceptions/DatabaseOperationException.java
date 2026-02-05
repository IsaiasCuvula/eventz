package com.bersyte.eventz.common.domain.exceptions;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super(message);
    }
}