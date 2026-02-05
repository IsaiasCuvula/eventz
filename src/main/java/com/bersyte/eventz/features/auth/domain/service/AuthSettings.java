package com.bersyte.eventz.features.auth.domain.service;

import java.time.Duration;

public interface AuthSettings {
    Duration getVerificationCodeExpiration();
}
