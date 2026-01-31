package com.bersyte.eventz.users;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
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
    private UserJpaRepository userJpaRepository;


    @BeforeEach
    void setUp() {

        List<EventEntity> events = List.of ();
        List<EventRegistrationEntity> registrations = List.of ();

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

        userJpaRepository.save (user);

    }

    @Test
    void shouldGetUserByEmail() {
        //Given
        String email = "isaias@gmail.com";

        //When
        Optional<UserEntity> userOptional = userJpaRepository.findByEmail (email);

        //Assert
        assertTrue (userOptional.isPresent ());
        assertEquals (email, userOptional.get ().getEmail ());
    }


    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        //Given
        String email = "doesnotexist@gmail.com";

        //Act
        Optional<UserEntity> userOptional = userJpaRepository.findByEmail (email);

        //Assert
        assertFalse (userOptional.isPresent ());
    }
}