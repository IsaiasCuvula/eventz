package com.bersyte.eventz.exceptions;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super(message);
    }
}