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
    
    public Event(String title, String description, String location, LocalDateTime date, Integer maxParticipants) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.location = location;
        this.maxParticipants = maxParticipants;
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
