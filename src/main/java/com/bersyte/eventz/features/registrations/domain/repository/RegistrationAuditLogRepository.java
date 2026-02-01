package com.bersyte.eventz.features.registrations.domain.repository;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationAuditLog;

public interface RegistrationAuditLogRepository {
    RegistrationAuditLog save(RegistrationAuditLog auditLog);
}
