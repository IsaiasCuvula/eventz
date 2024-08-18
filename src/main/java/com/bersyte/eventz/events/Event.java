package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.event_participation.EventParticipation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private Date date;
    private Date createdAt;

    @ManyToOne
    private AppUser organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventParticipation> registrations;
}
