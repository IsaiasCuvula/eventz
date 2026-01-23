package com.bersyte.eventz.features.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.features.event_participation.EventParticipation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime date;
    private Integer maxParticipants;
    private LocalDateTime updateAt;
    private LocalDateTime createdAt;

    @ManyToOne
    private AppUser organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventParticipation> participants;
}
