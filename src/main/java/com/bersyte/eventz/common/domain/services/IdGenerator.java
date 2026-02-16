package com.bersyte.eventz.common.domain.services;

public interface IdGenerator {
    String generateUuid();
    String generateCheckInToken();
}
