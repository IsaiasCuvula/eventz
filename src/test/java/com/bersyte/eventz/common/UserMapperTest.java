package com.bersyte.eventz.common;

import com.bersyte.eventz.auth.RegisterDto;
import com.bersyte.eventz.event_participation.EventParticipation;
import com.bersyte.eventz.events.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper ();
    }

    @Test
    public void shouldMapRegisterRequestDtoToAppUserEntity() {
        // Given
        RegisterDto dto = new RegisterDto (
                "may@gmail.com",
                "123456",
                UserRole.USER,
                "May",
                "Jane",
                "+244989647474",
                System.currentTimeMillis ()
        );

        // When
        AppUser user = userMapper.toUserEntity (dto);

        //Then - Assert
        assertNotNull (user);
        assertEquals (dto.email (), user.getEmail ());
        assertEquals (dto.password (), user.getPassword ());
        assertEquals (dto.role (), user.getRole ());
        assertEquals (dto.firstName (), user.getFirstName ());
        assertEquals (dto.lastName (), user.getLastName ());
        assertEquals (dto.phone (), user.getPhone ());
        assertEquals (dto.createdAt (), user.getCreatedAt ().getTime ());
    }

    @Test
    public void shouldMapUserEntityToUserResponseDto() {
        // Given
        List<Event> events = List.of ();
        List<EventParticipation> registrations = List.of ();

        AppUser user = new AppUser (
                1L,
                "may@gmail.com",
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

        // When
        UserResponseDto response = userMapper.toUserResponseDTO (user);

        //Then - Assert
        assertNotNull (user);
        assertEquals (user.getEmail (), response.email ());
        assertEquals (user.getRole (), response.role ());
        assertEquals (user.getFirstName (), response.firstName ());
        assertEquals (user.getLastName (), response.lastName ());
        assertEquals (user.getPhone (), response.phone ());
        assertEquals (user.getCreatedAt (), response.createdAt ());
        assertEquals (user.getId (), response.id ());
    }


    @Test
    public void shouldThrowExceptionWhenRegisterUserRequestDtoIsNull() {
        // Given
        Exception exception = assertThrows (
                NullPointerException.class, () -> userMapper.toUserEntity (null)
        );

        //Assert
        assertEquals ("Register dto cannot be null", exception.getMessage ());
    }

    @Test
    public void shouldThrowExceptionWhenUserEntityIsNull() {
        //Given
        Exception exception = assertThrows (
                NullPointerException.class, () -> userMapper.toUserResponseDTO (null)
        );

        //Assert
        assertEquals ("User cannot be null", exception.getMessage ());
    }
}