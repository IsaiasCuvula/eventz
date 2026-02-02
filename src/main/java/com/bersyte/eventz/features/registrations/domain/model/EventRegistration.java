package com.bersyte.eventz.features.registrations.domain.model;

import com.bersyte.eventz.common.domain.exceptions.BusinessException;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.users.domain.model.AppUser;

import java.time.LocalDateTime;

public class EventRegistration {
    private final String id;
    private RegistrationStatus status;
    private Event event;
    private AppUser user;
    private String checkInToken;
    private AppUser checkedInBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime checkedInAt;
    
    //enum payment status
    
    private EventRegistration(
            String id, RegistrationStatus status,String checkInToken,
            Event event, AppUser user, LocalDateTime createdAt,
            LocalDateTime updateAt
    ){
        this.id = id;
        this.event = event;
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updateAt;
        this.checkInToken = checkInToken;
    }
    
    public static EventRegistration create(
            String id,String checkInToken,
            Event event, AppUser user
    ){
        LocalDateTime now =     LocalDateTime.now();
        return new EventRegistration(
                id,  RegistrationStatus.ACTIVE,checkInToken,
                event, user,now , now
        );
    }
    
    public static EventRegistration restore(
            String id, RegistrationStatus status,String checkInToken,
            Event event, AppUser user, LocalDateTime createdAt,
            LocalDateTime updateAt
    ){
        return new EventRegistration(
                id, status,checkInToken, event, user,createdAt, updateAt
        );
    }
    
    public boolean canCheckIn(AppUser requester){
        return requester.isAdmin() || this.event.isOwnedBy(requester);
    }
    
    //Admin, event organizer, or the actual registered user can cancel his participation
    public boolean canManage(AppUser requester){
        return requester.isAdmin() || this.event.isOwnedBy(requester)|| this.user.getId().equals(requester.getId());
    }
    
    public EventRegistration markAsUsed(AppUser staff, LocalDateTime checkedInAt){
        if (this.status == RegistrationStatus.CANCELLED) {
            throw new BusinessException("Cannot check-in a cancelled ticket.");
        }
        if (this.status == RegistrationStatus.USED) {
            throw new BusinessException("This ticket has already been used.");
        }
        this.status = RegistrationStatus.USED;
        this.updatedAt = checkedInAt;
        this.checkedInAt = checkedInAt;
        this.checkedInBy = staff;
        return this;
    }
    
    public EventRegistration updateCheckInToken(String newCheckInToken, AppUser requester,LocalDateTime updatedAt) {
        if(!this.canCheckIn(requester) && !this.isTheUserItself(requester)){
            throw new BusinessException("You don't have permission to update this token.");
        }
        this.checkInToken = newCheckInToken;
        this.updatedAt =updatedAt;
        return this;
    }
    
    public EventRegistration cancel(AppUser requester, LocalDateTime now){
        if(this.status == RegistrationStatus.USED){
            throw new BusinessException("Cannot cancel a ticket that has already been used.");
        }
        if(this.status == RegistrationStatus.CANCELLED){
            throw new BusinessException("Cannot cancel a ticket that has already been canceled.");
        }
        this.status = RegistrationStatus.CANCELLED;
        this.updatedAt = now;
        return this;
    }
    
    public boolean isTheUserItself(AppUser user){
        return user.getId().equals(this.user.getId());
    }
    
    public AppUser getCheckedInBy() {
        return checkedInBy;
    }
    
    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }
    
    public String getCheckInToken() {
        return checkInToken;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
