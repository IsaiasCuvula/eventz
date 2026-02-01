package com.bersyte.eventz.features.registrations.domain.services;

import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationAuditLog;
import com.bersyte.eventz.features.registrations.domain.model.TicketSecurityAction;
import com.bersyte.eventz.features.registrations.domain.repository.RegistrationAuditLogRepository;

import java.time.LocalDateTime;

public class AuditService {
    private final RegistrationAuditLogRepository auditLogRepository;
    private final IdGenerator idGenerator;
    
    public AuditService(RegistrationAuditLogRepository auditLogRepository, IdGenerator idGenerator) {
        this.auditLogRepository = auditLogRepository;
        this.idGenerator = idGenerator;
    }
    
    
    public void logTokenRotation(
            String registrationId, String requesterId,
            String requesterName, String oldToken,
            String newToken, LocalDateTime createdAt
    ){
  
        RegistrationAuditLog auditLog = new RegistrationAuditLog(
                idGenerator.generateUuid(), registrationId, TicketSecurityAction.TOKEN_ROTATION,
                oldToken, newToken, requesterId,requesterName,createdAt
        );
        auditLogRepository.save(auditLog);
    
    }
}
