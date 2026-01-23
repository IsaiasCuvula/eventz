package com.bersyte.eventz.features.events.domain.model;

import java.time.LocalDateTime;

public class Event {
    private String id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer maxParticipants;
    private LocalDateTime updateAt;
    private LocalDateTime createdAt;
    
    public Event(String title, String description, String location, LocalDateTime date, Integer maxParticipants, LocalDateTime updateAt, LocalDateTime createdAt) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.maxParticipants = maxParticipants;
        this.updateAt = updateAt;
        this.createdAt = createdAt;
    }
    
    public Event(String id, String title, String description, String location, LocalDateTime date, Integer maxParticipants, LocalDateTime updateAt, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.maxParticipants = maxParticipants;
        this.updateAt = updateAt;
        this.createdAt = createdAt;
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
}
