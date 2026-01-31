package com.bersyte.eventz.features.registrations.infrastructure.persistence.entities;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_registration")
public class EventRegistrationEntity {
    @Id
    String id;

    @Enumerated(EnumType.STRING)
    RegistrationStatus status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    EventEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;
    LocalDateTime registeredAt;
    LocalDateTime updateAt;
}
