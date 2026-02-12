package com.bersyte.eventz.events;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.mappers.EventEntityMapper;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventEntityMapperTest {

    private EventEntityMapper eventEntityMapper;

    @BeforeEach
    void setUp() {
        eventEntityMapper = new EventEntityMapper();
    }


    @Test
    void shouldMapEventRequestDtoToEventEntity() {
        //Arrange
        CreateEventRequest dto = new CreateEventRequest(
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3",
                1726472944000L,
                1726172944000L
        );

        //When
        EventEntity event = eventEntityMapper.toEventEntity (dto);

        //Assert
        assertNotNull (event);
        assertEquals (dto.title (), event.getTitle ());
        assertEquals (dto.description (), event.getDescription ());
        assertEquals (dto.location (), event.getLocation ());
        assertEquals (new Date (dto.date ()), event.getDate ());
        assertEquals (new Date (dto.createdAt ()), event.getCreatedAt ());
    }

    @Test
    void shouldReturnExceptionWhenThereIsNoEventRequestDto() {
        //Arrange

        //When
        NullPointerException exception = assertThrows (
                NullPointerException.class,
                () -> eventEntityMapper.toEventEntity (null)
        );

        //Assert
        assertEquals ("Request data cannot be null", exception.getMessage ());
    }


    @Test
    void shouldMapEventEntityToEventResponseDto() {
        // Arrange
        EventEntity event = new EventEntity(
                2L,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3",
                new Date (1726472944000L),
                new Date (),
                getOrganizerForTest (),
                List.of ()
        );

        //When
        EventResponse responseDto = eventEntityMapper.toDomain(event);

        //Assert
        assertNotNull (responseDto);
        assertEquals (responseDto.title (), event.getTitle ());
        assertEquals (responseDto.description (), event.getDescription ());
        assertEquals (responseDto.location (), event.getLocation ());
        assertEquals (new Date (1726472944000L), event.getDate ());
        assertEquals (responseDto.createdAt (), event.getCreatedAt ());
    }


    @Test
    void shouldReturnExceptionWhenThereIsNoEventEntity() {
        //Arrange

        //When
        NullPointerException exception = assertThrows (
                NullPointerException.class,
                () -> eventEntityMapper.toDomain(null)
        );

        //Assert
        assertEquals ("Entity cannot be null", exception.getMessage ());
    }


    private UserEntity getOrganizerForTest() {
        return new UserEntity(
                2L,
                "isaias@gmail.com",
                "123456",
                "May",
                "Jane",
                "+244989647474",
                new Date (),
                UserRole.USER,
                List.of (),
                List.of (),
                "123456",
                LocalDateTime.now (),
                true
        );
    }
}