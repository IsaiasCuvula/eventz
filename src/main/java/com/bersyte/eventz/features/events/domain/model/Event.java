package com.bersyte.eventz.features.events.domain.model;

import com.bersyte.eventz.features.users.domain.model.AppUser;

import java.time.LocalDateTime;

public class Event {
    private final String id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer maxParticipants;
    private LocalDateTime updateAt;
    private final LocalDateTime createdAt;
    private AppUser organizer;
    private Integer participantsCount;
  
    private Event(String id, String title, String description,
                 String location, LocalDateTime date,
                 Integer maxParticipants, LocalDateTime updateAt,
                 LocalDateTime createdAt,
                 AppUser organizer,
                 Integer participantsCount
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.maxParticipants = maxParticipants;
        this.updateAt = updateAt;
        this.createdAt = createdAt;
        this.organizer = organizer;
        this.participantsCount = participantsCount;
    }
    
    public static Event create(
            String id, String title, String description,
            String location, LocalDateTime date,
            Integer maxParticipants,
            AppUser organizer
    ) {
     return new Event(
             id,title,description, location,date,
             maxParticipants,
             LocalDateTime.now(),
             LocalDateTime.now(),
             organizer,
             0
     );
    }
    
    public static Event restore(
            String id, String title, String description,
            String location, LocalDateTime date,
            Integer maxParticipants,
            LocalDateTime createdAt, LocalDateTime updateAt,
            AppUser organizer,
            Integer participantsCount
    ){
        return new Event(
                id,title,description, location,date,
                maxParticipants,
                updateAt,
                createdAt,
                organizer,
                participantsCount
        );
    }
    
    public boolean canAcceptMoreParticipants() {
        return participantsCount < maxParticipants;
    }
    
    public boolean isOwnedBy(AppUser owner){
        return this.organizer.getId().equals(owner.getId());
    }
    
    
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    
    public AppUser getOrganizer() {
        return organizer;
    }
    
    public Integer getParticipantsCount() {
        return participantsCount;
    }
    
    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }
    
    public void setOrganizer(AppUser organizer) {
        this.organizer = organizer;
    }
    
    public String getId() {
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
    
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
