package com.bersyte.eventz.common;

import com.bersyte.eventz.common.domain.exceptions.DatabaseOperationException;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.mappers.EventEntityMapper;
import com.bersyte.eventz.features.events.infrastructure.persistence.repositories.EventJpaRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EventCommonServiceTest {

    @InjectMocks
    private EventCommonService eventCommonService;
    @Mock
    private EventJpaRepository eventJpaRepository;
    @Mock
    private EventEntityMapper eventEntityMapper;

    @Test
    void shouldFindEventByIdSuccessfully() {
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

        //Mock calls
        Mockito.when (eventJpaRepository.findById (2L))
                .thenReturn (Optional.of (event));

        //When
        EventEntity foundEvent = eventCommonService.findEventById (2L);

        //Act - Assert
        assertEquals (event, foundEvent);
    }

    @Test
    public void shouldThrowExceptionWhenEventNotFound() {

        //Arrange
        Long eventId = 4L;

        //When
        DatabaseOperationException exception = assertThrows (
                DatabaseOperationException.class,
                () -> eventCommonService.findEventById (eventId)
        );

        //Act - Assert
        assertEquals ("Could not find event with eventId: " + eventId, exception.getMessage ());
    }

    @Test
    void shouldUpdateEventOnDbSuccessfully() {
        //Arrange
        Long id = 2L;
        String title = "Training Workshop";
        String description = "Training session on new software tools";
        String location = "Training Room 3";
        Date eventDate = new Date (1726472944000L);
        Date createdAt = new Date ();
        UserEntity organizer = getOrganizerForTest ();

        CreateEventRequest dto = new CreateEventRequest(
                title,
                description,
                location,
                eventDate.getTime (),
                createdAt.getTime ()
        );

        EventResponse response = new EventResponse(
                id,
                title,
                description,
                location,
                eventDate,
                organizer.getFirstName (),
                List.of (),
                createdAt
        );

        EventEntity event = new EventEntity(
                id,
                dto.title (),
                dto.description (),
                dto.location (),
                new Date (dto.date ()),
                new Date (dto.createdAt ()),
                organizer,
                List.of ()
        );

        // Mock calls
        Mockito.when (eventJpaRepository.save (event))
                .thenReturn (event);

        Mockito.when (eventEntityMapper.toDomain(event))
                .thenReturn (response);

        //When
        EventResponse updatedEvent = eventCommonService.updateEventOnDb (event, dto);

        //
        assertEquals (response, updatedEvent);
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