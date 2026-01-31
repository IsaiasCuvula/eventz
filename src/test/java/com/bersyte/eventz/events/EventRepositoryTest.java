package com.bersyte.eventz.events;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.repositories.EventJpaRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
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
    private EventJpaRepository eventJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @AfterEach
    void tearDown() {
        eventJpaRepository.deleteAll ();
    }


    @Test
    void shouldReturnAllUpcomingEvents() {
        // Given
        saveEventsForTest ();
        Pageable pageable = PageRequest.of (0, 10);

        //When
        Page<EventEntity> eventsPerPage = eventJpaRepository.findUpcomingEvents (
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
        Page<EventEntity> eventsPerPage = eventJpaRepository.findByDateBetween(
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
        Page<EventEntity> eventsPerPage = eventJpaRepository.filterEvents(
                "New Launch EventEntity", "Luanda", pageable
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
                "New Launch EventEntity",
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
        Page<EventEntity> eventsPerPage = eventJpaRepository.filterEvents(
                "Naval Academy", "", pageable
        );

        //Act - Assert
        assertEquals (0, eventsPerPage.getTotalElements ());
    }

    private void saveEventsForTest() {

        List<EventRegistrationEntity> participants = List.of ();
        UserEntity organizer = getOrganizerForTest ();

        //past event
        EventEntity event = new EventEntity();
        event.setId (1L);
        event.setOrganizer (organizer);
        event.setTitle ("Product Launch EventEntity");
        event.setDescription ("Launch of new product line X");
        event.setLocation ("City Convention Center");
        event.setDate (new Date (1723276144000L));
        event.setCreatedAt (new Date ());
        event.setParticipants (participants);

        //upcoming event
        EventEntity event2 = new EventEntity();
        event2.setId (2L);
        event2.setOrganizer (organizer);
        event2.setTitle ("New Launch EventEntity");
        event2.setDescription ("Launch of X line in Luanda");
        event2.setLocation ("Luanda City Center");
        event2.setDate (new Date (System.currentTimeMillis () + 604800000));
        event2.setCreatedAt (new Date ());
        event2.setParticipants (participants);

        eventJpaRepository.saveAll (List.of (event, event2));

    }


    private UserEntity getOrganizerForTest() {
        List<EventRegistrationEntity> registrations = List.of ();
        List<EventEntity> events = List.of ();
        UserEntity user = new UserEntity(
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

        return userJpaRepository.save (user);
    }


}