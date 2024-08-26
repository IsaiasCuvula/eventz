package com.bersyte.eventz.common;

import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.events.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

class AppUserTest {

    @BeforeEach
    void setUp() {

        List<Event> events = List.of ();
        List<EventParticipation> registrations = List.of ();

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
    }

    @Test
    public void shouldReturnTheRightUserName() {

    }

}