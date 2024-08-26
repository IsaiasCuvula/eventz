package com.bersyte.eventz.users;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.events.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


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

        userRepository.save (user);

    }

    @Test
    void shouldGetUserByEmail() {
        //Given
        String email = "isaias@gmail.com";

        //When
        Optional<AppUser> userOptional = userRepository.findByEmail (email);

        //Assert
        assertTrue (userOptional.isPresent ());
        assertEquals (email, userOptional.get ().getEmail ());
    }


    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        //Given
        String email = "doesnotexist@gmail.com";

        //Act
        Optional<AppUser> userOptional = userRepository.findByEmail (email);

        //Assert
        assertFalse (userOptional.isPresent ());
    }
}