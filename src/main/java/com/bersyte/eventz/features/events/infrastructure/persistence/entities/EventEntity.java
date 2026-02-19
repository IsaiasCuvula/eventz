package com.bersyte.eventz.features.events.infrastructure.persistence.entities;

import com.bersyte.eventz.features.events.domain.model.EventAccessType;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class EventEntity {
    @Id
    private UUID id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer maxParticipants;
    
    private EventAccessType accessType;
    
    private Integer price;
    
    @LastModifiedDate
    private LocalDateTime updateAt;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private UserEntity organizer;

    @OneToMany(mappedBy = "event")
    private List<EventRegistrationEntity> participants;
    
    private Integer participantsCount;
}
