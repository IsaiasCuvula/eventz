package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll ();
    }


    @Test
    void shouldReturnAllUpcomingEvents() {
        // Given
        saveEventsForTest ();
        Pageable pageable = PageRequest.of (0, 10);

        //When
        Page<Event> eventsPerPage = eventRepository.findUpcomingEvents (
                new Date (), pageable
        );

        //Act - Assert
        assertEquals (1, eventsPerPage.getTotalElements ());
    }

    @Test
    void shouldReturnAllEventsByGivenDate() {
        // Given
        saveEventsForTest ();
        Date date = new Date (1723276144000L);
        Pageable pageable = PageRequest.of (0, 10);

        //When
        Page<Event> eventsPerPage = eventRepository.findEventsByDate (
                date, pageable
        );

        //Act - Assert
        assertEquals (1, eventsPerPage.getTotalElements ());
    }

    @Test
    void shouldReturnAllEventsByGivenTitleAndLocation() {
        // Given
        saveEventsForTest ();
        Pageable pageable = PageRequest.of (0, 10);

        //When
        Page<Event> eventsPerPage = eventRepository.filterEventsByTitleAndLocation (
                "New Launch Event", "Luanda", pageable
        );

        //Act - Assert
        assertEquals (1, eventsPerPage.getTotalElements ());
        assertEquals (
                "Luanda City Center",
                eventsPerPage
                        .get ()
                        .toList ()
                        .getFirst ()
                        .getLocation ()
        );

        assertEquals (
                "New Launch Event",
                eventsPerPage
                        .get ()
                        .toList ()
                        .getFirst ()
                        .getTitle ()
        );
    }


    @Test
    void shouldReturnNoEventsIfGivenTitleAndLocationDoesNotExists() {
        // Given
        Pageable pageable = PageRequest.of (0, 10);

        //When
        Page<Event> eventsPerPage = eventRepository.filterEventsByTitleAndLocation (
                "Naval Academy", "", pageable
        );

        //Act - Assert
        assertEquals (0, eventsPerPage.getTotalElements ());
    }

    private void saveEventsForTest() {

        List<EventParticipation> participants = List.of ();
        AppUser organizer = getOrganizerForTest ();

        //past event
        Event event = new Event ();
        event.setId (1L);
        event.setOrganizer (organizer);
        event.setTitle ("Product Launch Event");
        event.setDescription ("Launch of new product line X");
        event.setLocation ("City Convention Center");
        event.setDate (new Date (1723276144000L));
        event.setCreatedAt (new Date ());
        event.setParticipants (participants);

        //upcoming event
        Event event2 = new Event ();
        event2.setId (2L);
        event2.setOrganizer (organizer);
        event2.setTitle ("New Launch Event");
        event2.setDescription ("Launch of X line in Luanda");
        event2.setLocation ("Luanda City Center");
        event2.setDate (new Date (System.currentTimeMillis () + 604800000));
        event2.setCreatedAt (new Date ());
        event2.setParticipants (participants);

        eventRepository.saveAll (List.of (event, event2));

    }


    private AppUser getOrganizerForTest() {
        List<EventParticipation> registrations = List.of ();
        List<Event> events = List.of ();
        AppUser user = new AppUser (
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

        return userRepository.save (user);
    }


}