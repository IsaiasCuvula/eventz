package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        List<EventParticipation> participants = List.of ();
        List<EventParticipation> registrations = List.of ();
        List<Event> events = List.of ();


        AppUser organizer = new AppUser (
                1L,
                "isaias@gmail.com",
                "123456",
                "May",
                "Jane",
                "+244989647474",
                new Date (),
                UserRole.USER,
                events,
                registrations,
                "123456",
                LocalDateTime.now (),
                true
        );

        userRepository.save (organizer);

        //past event
        Event event = new Event (
                1L,
                "Product Launch Event",
                "Launch of new product line X",
                "City Convention Center",
                new Date (System.currentTimeMillis () - 604800000),
                new Date (),
                organizer,
                participants
        );

        //upcoming event
        Event event2 = new Event (
                2L,
                "New Launch Event",
                "Launch of X line",
                "Luanda",
                new Date (System.currentTimeMillis () + 604800000),
                new Date (),
                organizer,
                participants
        );

        eventRepository.saveAll (List.of (event, event2));
    }


    @Test
    void shouldReturnAllUpcomingEvents() {
        // Given
        Pageable pageable = PageRequest.of (0, 10);

        //When
        Page<Event> eventsPerPage = eventRepository.getUpcomingEvents (
                new Date (), pageable
        );

        //Act - Assert
        assertEquals (1, eventsPerPage.getTotalElements ());
    }

}