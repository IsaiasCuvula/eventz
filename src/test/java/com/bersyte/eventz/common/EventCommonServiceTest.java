package com.bersyte.eventz.common;

import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.features.events.*;
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
    private EventRepository eventRepository;
    @Mock
    private EventMappers eventMappers;

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
        Mockito.when (eventRepository.findById (2L))
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
        assertEquals ("Could not find event with id: " + eventId, exception.getMessage ());
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
        AppUser organizer = getOrganizerForTest ();

        EventRequestDto dto = new EventRequestDto (
                title,
                description,
                location,
                eventDate.getTime (),
                createdAt.getTime ()
        );

        EventResponseDto response = new EventResponseDto (
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
        Mockito.when (eventRepository.save (event))
                .thenReturn (event);

        Mockito.when (eventMappers.toResponseDTO (event))
                .thenReturn (response);

        //When
        EventResponseDto updatedEvent = eventCommonService.updateEventOnDb (event, dto);

        //
        assertEquals (response, updatedEvent);
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