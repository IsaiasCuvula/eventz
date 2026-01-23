package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.features.events.EventEntity;
import com.bersyte.eventz.features.events.EventMappers;
import com.bersyte.eventz.features.events.EventRequestDto;
import com.bersyte.eventz.features.events.EventResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventMappersTest {

    private EventMappers eventMappers;

    @BeforeEach
    void setUp() {
        eventMappers = new EventMappers ();
    }


    @Test
    void shouldMapEventRequestDtoToEventEntity() {
        //Arrange
        EventRequestDto dto = new EventRequestDto (
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3",
                1726472944000L,
                1726172944000L
        );

        //When
        EventEntity event = eventMappers.toEventEntity (dto);

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
                () -> eventMappers.toEventEntity (null)
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
        EventResponseDto responseDto = eventMappers.toResponseDTO (event);

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
                () -> eventMappers.toResponseDTO (null)
        );

        //Assert
        assertEquals ("Entity cannot be null", exception.getMessage ());
    }


    private AppUser getOrganizerForTest() {
        return new AppUser (
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