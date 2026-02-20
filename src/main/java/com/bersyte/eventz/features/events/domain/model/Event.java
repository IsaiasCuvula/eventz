package com.bersyte.eventz.features.events.domain.model;

import com.bersyte.eventz.features.events.application.dtos.UpdateEventRequest;
import com.bersyte.eventz.features.users.domain.model.AppUser;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private final UUID id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer maxParticipants;
    private LocalDateTime updatedAt;
    private final LocalDateTime createdAt;
    private AppUser organizer;
    private Integer price; //price in cent
    private EventAccessType accessType;
    private Integer participantsCount;
    

  
    private Event(UUID id, String title, String description,
                 String location, LocalDateTime date,
                 Integer maxParticipants, LocalDateTime updateAt,
                 LocalDateTime createdAt, AppUser organizer,
                 Integer participantsCount,EventAccessType access, Integer price
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.maxParticipants = maxParticipants;
        this.updatedAt = updateAt;
        this.createdAt = createdAt;
        this.organizer = organizer;
        this.participantsCount = participantsCount;
        this.price = price;
        this.accessType = access;
    }
    
    public static Event create(
            UUID id, String title, String description,
            String location, LocalDateTime date,
            Integer maxParticipants,
            AppUser organizer,
            Integer price,LocalDateTime createdAt
    ) {
     EventAccessType accessType = price == 0? EventAccessType.FREE: EventAccessType.PAID;
     return new Event(
             id,title,description, location,date,
             maxParticipants, createdAt, createdAt,
             organizer, 0,accessType, price
         );
    }
    
    public static Event restore(
            UUID id, String title, String description,
            String location, LocalDateTime date,
            Integer maxParticipants,LocalDateTime createdAt, LocalDateTime updateAt,
            AppUser organizer, Integer participantsCount,EventAccessType access,
            Integer price
    ){
        return new Event(
                id,title,description, location,date,
                maxParticipants,
                updateAt,
                createdAt,
                organizer,
                participantsCount,
                access, price
        );
    }
    
    public Event updateDetails(UpdateEventRequest request, LocalDateTime updatedAt){
        if(request.title() != null) this.title = request.title();
        if(request.description() != null) this.description = request.description();
        if(request.location() != null) this.location = request.location();
        if(request.date() != null) this.date = request.date();
        if(request.maxParticipants() != null) this.maxParticipants = request.maxParticipants();
        
        //Update only if the event is already paid
        if(request.price() != null && request.price() >= 0 && this.isPaid()){
            this.price = request.price();
        }
        this.updatedAt = updatedAt;
        return this;
    }
    
    public boolean isPaid() {
        return this.accessType == EventAccessType.PAID;
    }
    
    public Integer getPrice() {
        return price;
    }
    
    public EventAccessType getAccessType() {
        return accessType;
    }
    
    public boolean canManage(AppUser user) {
        return user.isAdmin() || this.isOwnedBy(user);
    }
    
    public boolean canAcceptMoreParticipants() {
        return participantsCount < maxParticipants;
    }
    
    public boolean isOwnedBy(AppUser owner){
        return this.organizer.getId().equals(owner.getId());
    }
    
    public AppUser getOrganizer() {
        return organizer;
    }
    
    public Integer getParticipantsCount() {
        return participantsCount;
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public Integer getMaxParticipants() {
        return maxParticipants;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
}
