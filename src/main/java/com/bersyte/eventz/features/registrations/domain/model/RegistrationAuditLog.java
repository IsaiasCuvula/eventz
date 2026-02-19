package com.bersyte.eventz.features.registrations.domain.model;


import java.time.LocalDateTime;
import java.util.UUID;

public class RegistrationAuditLog {
    private final UUID id;
    private final UUID registrationId;
    private final AuditAction action;
    private final String oldToken;
    private final String newToken;
    private final UUID actorId;
    private final String actorName;
    private final LocalDateTime createdAt;
    
    public RegistrationAuditLog(
            UUID id, UUID registrationId,
            AuditAction action, String oldToken,
            String newToken, UUID actorId, String actorName, LocalDateTime createdAt
    ) {
        this.id = id;
        this.registrationId = registrationId;
        this.action = action;
        this.oldToken = oldToken;
        this.newToken = newToken;
        this.actorId = actorId;
        this.createdAt = createdAt;
        this.actorName = actorName;
    }
    
    public UUID getId() {
        return id;
    }
    
    public UUID getRegistrationId() {
        return registrationId;
    }
    
    public AuditAction getAction() {
        return action;
    }
    
    public String getOldToken() {
        return oldToken;
    }
    
    public String getNewToken() {
        return newToken;
    }
    
    public UUID getActorId() {
        return actorId;
    }
    
    public String getActorName() {
        return actorName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
