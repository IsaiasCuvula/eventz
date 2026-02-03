package com.bersyte.eventz.common;

import com.bersyte.eventz.features.auth.application.dtos.RegisterDto;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserEntityMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserEntityMapper();
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
        UserEntity user = userMapper.toUserEntity (dto);

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
        List<EventEntity> events = List.of ();
        List<EventRegistrationEntity> registrations = List.of ();

        UserEntity user = new UserEntity(
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
        UserResponse response = userMapper.toUserResponse(user);

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
                NullPointerException.class, () -> userMapper.toUserResponse(null)
        );

        //Assert
        assertEquals ("User cannot be null", exception.getMessage ());
    }
}