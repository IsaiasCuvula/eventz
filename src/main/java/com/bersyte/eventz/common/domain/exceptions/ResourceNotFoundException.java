package com.bersyte.eventz.common.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String id) {
        super(String.format("%s with id: %s not found", resourceName, id));
    }
}
