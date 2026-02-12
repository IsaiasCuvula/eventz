package com.bersyte.eventz.features.auth.domain.service;

import java.time.Duration;

public interface AuthProperties {
    Duration getVerificationCodeExpiration();
}
