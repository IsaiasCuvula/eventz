package com.bersyte.eventz.features.registrations.domain.model;


import java.time.LocalDateTime;

public class RegistrationAuditLog {
    private final String id;
    private final String registrationId;
    private final AuditAction action;
    private final String oldToken;
    private final String newToken;
    private final String actorId;
    private final String actorName;
    private final LocalDateTime createdAt;
    
    public RegistrationAuditLog(
            String id, String registrationId,
            AuditAction action, String oldToken,
            String newToken, String actorId, String actorName, LocalDateTime createdAt
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
    
    public String getActorName() {
        return actorName;
    }
    
    public String getRegistrationId() {
        return registrationId;
    }
    
    public String getActorId() {
        return actorId;
    }
    
    public String getId() {
        return id;
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
    

    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
