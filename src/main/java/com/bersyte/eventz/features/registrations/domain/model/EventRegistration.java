package com.bersyte.eventz.features.registrations.domain.model;

import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.users.domain.model.AppUser;

import java.time.LocalDateTime;

public class EventRegistration {
    String id;
    RegistrationStatus status;
    Event event;
    AppUser user;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
    
    //enum payment status
    
    private EventRegistration(
            String id, RegistrationStatus status,
            Event event, AppUser user, LocalDateTime createdAt,
            LocalDateTime updateAt
    ){
        this.id = id;
        this.event = event;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
    
    public static EventRegistration create(
            String id,
            Event event, AppUser user
    ){
        LocalDateTime now =     LocalDateTime.now();
        return new EventRegistration(id,  RegistrationStatus.ACTIVE,
                event, user,now , now
        );
    }
    
    public static EventRegistration restore(
            String id, RegistrationStatus status,
            Event event, AppUser user, LocalDateTime createdAt,
            LocalDateTime updateAt
    ){
        return new EventRegistration(
                id, status, event, user,createdAt, updateAt
        );
    }
    
    
    public String getId() {
        return id;
    }
    
    public RegistrationStatus getStatus() {
        return status;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public AppUser getUser() {
        return user;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
    
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
