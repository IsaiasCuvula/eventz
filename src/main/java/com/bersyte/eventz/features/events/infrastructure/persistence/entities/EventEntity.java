package com.bersyte.eventz.features.events.infrastructure.persistence.entities;

import com.bersyte.eventz.common.UserEntity;
import com.bersyte.eventz.features.event_participation.EventParticipation;
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

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class EventEntity {
    @Id
    private String id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer maxParticipants;
    
    @LastModifiedDate
    private LocalDateTime updateAt;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private UserEntity organizer;

    @OneToMany(mappedBy = "event")
    private List<EventParticipation> participants;
    
    Integer participantsCount;
}
