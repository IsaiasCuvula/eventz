package com.bersyte.eventz.features.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.features.events.EventEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_participations")
public class EventParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ParticipationStatus status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    EventEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    AppUser user;
    Date registeredAt;
    Date updateAt;
}
