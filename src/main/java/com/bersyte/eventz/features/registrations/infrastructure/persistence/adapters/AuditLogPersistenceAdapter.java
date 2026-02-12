package com.bersyte.eventz.features.registrations.infrastructure.persistence.adapters;


import com.bersyte.eventz.features.registrations.domain.model.RegistrationAuditLog;
import com.bersyte.eventz.features.registrations.domain.repository.RegistrationAuditLogRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogPersistenceAdapter implements RegistrationAuditLogRepository {
    
    
    @Override
    public RegistrationAuditLog save(RegistrationAuditLog auditLog) {
        return null;
    }
}
