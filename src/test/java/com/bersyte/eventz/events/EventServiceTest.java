package com.bersyte.eventz.events;

import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.common.presentation.exceptions.DatabaseOperationException;
import com.bersyte.eventz.features.events.application.dtos.CreateEventRequest;
import com.bersyte.eventz.features.events.application.dtos.EventResponse;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.mappers.EventEntityMapper;
import com.bersyte.eventz.features.events.infrastructure.persistence.repositories.EventJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {


    @InjectMocks
    private EventService eventService;

    @Mock
    private EventJpaRepository eventJpaRepository;
    @Mock
    private UserService usersService;
    @Mock
    private EventCommonService eventCommonService;
    @Mock
    private EventEntityMapper eventEntityMapper;
    @Mock
    private UserDetails userDetails;

    @Test
    void shouldSuccessfullyCreateEvent() {
        //Arrange
        String email = "isaias@gmail.com";
        String title = "Training Workshop";
        String description = "Training session on new software tools";
        String location = "Training Room 3";
        long eventDate = new Date (1726472944000L).getTime ();
        long createdAt = new Date ().getTime ();
        UserEntity organizer = getOrganizerForTest ();

        CreateEventRequest dto = new CreateEventRequest(
                title,
                description,
                location,
                eventDate,
                createdAt
        );

        EventEntity event = new EventEntity(
                2L,
                title,
                description,
                location,
                new Date (1726472944000L),
                new Date (1721472944000L),
                organizer,
                List.of ()
        );

        EventEntity savedEvent = new EventEntity(
                2L,
                title,
                description,
                location,
                new Date (1726472944000L),
                new Date (1721472944000L),
                organizer,
                List.of ()
        );

        // Mock the calls
        Mockito.when (userDetails.getUsername ())
                .thenReturn (email);

        Mockito.when (usersService.getUserByEmail (email))
                .thenReturn (organizer);

        Mockito.when (eventEntityMapper.toEventEntity (dto))
                .thenReturn (event);

        Mockito.when (eventJpaRepository.save (event))
                .thenReturn (savedEvent);

        Mockito.when (eventEntityMapper.toDomain(savedEvent))
                .thenReturn (
                        new EventResponse(
                                2L,
                                title,
                                description,
                                location,
                                new Date (1726472944000L),
                                organizer.getFirstName (),
                                List.of (),
                                new Date (1721472944000L)
                        )
                );

        //When
        EventResponse responseDto = eventService.createEvent (dto, userDetails);

        //Assert
        assertNotNull (responseDto);

        assertEquals (dto.title (), responseDto.title ());

        verify (eventEntityMapper, times (1))
                .toEventEntity (dto);

        verify (eventJpaRepository, times (1))
                .save (event);

        verify (eventEntityMapper, times (1))
                .toDomain(savedEvent);

    }

    @Test
    void shouldGetAllEventsSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of (page, size);
        EventEntity event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        EventEntity event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        EventEntity event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponse response = new EventResponse(
                9L,
                "title",
                "description",
                "location",
                new Date (1726472944000L),
                getOrganizerForTest ().getFirstName (),
                List.of (),
                new Date (1726172944000L)
        );

        List<EventEntity> events = List.of (event1, event2, event3);
        Page<EventEntity> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventJpaRepository.findAll (pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventEntityMapper.toDomain(any ()))
                .thenReturn (response);

        //When
        List<EventResponse> result = eventService.getAllEvents (page, size);

        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventEntityMapper, times (result.size ()))
                .toDomain(any ());

        verify (eventJpaRepository, times (1))
                .findAll (pageable);
    }


    @Test
    void shouldGetAllUpcomingEventsSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Date date = new Date (1724831344000L);
        Pageable pageable = PageRequest.of (page, size);
        EventEntity event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        EventEntity event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        EventEntity event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponse response = new EventResponse(
                9L,
                "title",
                "description",
                "location",
                date,
                getOrganizerForTest ().getFirstName (),
                List.of (),
                new Date (1726172944000L)
        );

        List<EventEntity> events = List.of (event1, event2, event3);
        Page<EventEntity> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventJpaRepository.findUpcomingEvents (date, pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventEntityMapper.toDomain(any ()))
                .thenReturn (response);

        //When
        List<EventResponse> result =
                eventService.getUpcomingEvents (date, page, size);

        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventEntityMapper, times (result.size ()))
                .toDomain(any ());

        verify (eventJpaRepository, times (1))
                .findUpcomingEvents (date, pageable);
    }

    @Test
    public void shouldGetEventByIdSuccessfully() {
        //Arrange
        Long eventId = 4L;

        EventEntity event1 = getEvent (
                eventId,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        EventResponse response = new EventResponse(
                eventId,
                event1.getTitle (),
                event1.getDescription (),
                event1.getLocation (),
                event1.getDate (),
                event1.getOrganizer ().getFirstName (),
                List.of (),
                event1.getCreatedAt ()
        );

        //Mock call
        Mockito.when (eventCommonService.findEventById (eventId))
                .thenReturn (event1);

        Mockito.when (eventEntityMapper.toDomain(event1))
                .thenReturn (response);

        //When
        EventResponse result = eventService.getEventById (eventId);

        assertNotNull (result);
        assertEquals (response, result);
    }

    @Test
    public void shouldReturnExceptionIfEventByIdNotFound() {
        // Arrange
        String expectedMsg = "Error while updating event: ";
        Long eventId = 56L;

        //Mock calls
        Mockito.when (eventCommonService.findEventById (eventId))
                .thenThrow (new DatabaseOperationException (expectedMsg));

        //When
        DatabaseOperationException exception = assertThrows (
                DatabaseOperationException.class,
                () -> eventService.getEventById (eventId)
        );

        //Act - Assert
        assertEquals (expectedMsg, exception.getMessage ());
    }

    @Test
    void should_update_event_successfully() {
        // Arrange
        Long eventId = 4L;
        String email = "isaias@gmail.com";
        String title = "Training Workshop";
        String description = "Training session on new software tools";
        String location = "Training Room 3";
        Date eventDate = new Date (1726472944000L);
        Date createdAt = new Date (1721472944000L);

        CreateEventRequest dto = new CreateEventRequest(
                title,
                description,
                location,
                eventDate.getTime (),
                createdAt.getTime ()
        );

        EventEntity event = getEvent (
                eventId,
                title,
                description,
                location
        );

        EventResponse response = new EventResponse(
                eventId,
                event.getTitle (),
                event.getDescription (),
                event.getLocation (),
                event.getDate (),
                event.getOrganizer ().getFirstName (),
                List.of (),
                event.getCreatedAt ()
        );

        //Mock calls
        Mockito.when (userDetails.getUsername ())
                .thenReturn (email);

        Mockito.when (eventCommonService.findEventById (eventId))
                .thenReturn (event);

        Mockito.when (eventCommonService.updateEventOnDb (event, dto))
                .thenReturn (response);

        //When
        EventResponse result = eventService.updateEvent (eventId, dto, userDetails);

        //Assert
        assertNotNull (result);

        assertEquals (response, result);
    }

    @Test
    public void shouldThrowAccessDeniedExceptionIfUserIsNotEventOrganizer() {
        //Arrange
        Long eventId = 4L;
        String expectedMsg = "You do not have permission to update this event";
        String email = "bernardoo@gmail.com";

        CreateEventRequest dto = new CreateEventRequest(
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3",
                new Date (1726472944000L).getTime (),
                new Date (1721472944000L).getTime ()
        );

        EventEntity event = getEvent (
                eventId,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        //Mock
        Mockito.when (userDetails.getUsername ())
                .thenReturn (email);

        Mockito.when (eventCommonService.findEventById (eventId))
                .thenReturn (event);

        //When
        AccessDeniedException exception = assertThrows (
                AccessDeniedException.class,
                () -> eventService.updateEvent (eventId, dto, userDetails)
        );

        //Assert
        assertEquals (expectedMsg, exception.getMessage ());
        verify (eventJpaRepository, never ()).delete (event);
    }

    @Test
    void shouldDeleteEventSuccessfully() {
        //Arrange
        String email = "isaias@gmail.com";
        Long eventId = 4L;
        EventEntity event = getEvent (
                eventId,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        //Mock call
        Mockito.when (userDetails.getUsername ())
                .thenReturn (email);

        Mockito.when (eventCommonService.findEventById (eventId))
                .thenReturn (event);

        //When
        eventService.deleteEvent (eventId, userDetails);

        //Assert
        verify (eventJpaRepository, times (1))
                .delete (event);
    }

    @Test
    public void shouldThrowAccessDeniedExceptionWhenUserNotAuthorizedToDeleteEvent() {
        //Arrange
        Long eventId = 4L;
        String expectedMsg = "You do not have permission to cancel this event";
        String email = "bernardo@gmail.com";
        EventEntity event = getEvent (
                eventId,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        //Mock
        Mockito.when (userDetails.getUsername ()).thenReturn (email);

        Mockito.when (eventCommonService.findEventById (eventId))
                .thenReturn (event);

        //When
        AccessDeniedException exception = assertThrows (
                AccessDeniedException.class,
                () -> eventService.deleteEvent (eventId, userDetails)
        );

        //Act & Assert
        assertEquals (expectedMsg, exception.getMessage ());

        verify (eventJpaRepository, never ()).delete (event);
    }

    @Test
    public void shouldGetEventsFilteredByTitleAndDescriptionSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Date date = new Date (1724831344000L);
        Pageable pageable = PageRequest.of (page, size);
        String title = "Training";
        String location = "Classroom";
        EventEntity event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Classroom 3"
        );

        EventEntity event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        EventEntity event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponse response = new EventResponse(
                9L,
                "title",
                "description",
                "location",
                date,
                getOrganizerForTest ().getFirstName (),
                List.of (),
                date
        );

        List<EventEntity> events = List.of (event1, event2, event3);
        Page<EventEntity> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventJpaRepository.filterEvents(title, location, pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventEntityMapper.toDomain(any ()))
                .thenReturn (response);

        //When
        List<EventResponse> result =
                eventService.getFilteredEventsByTitleAndLocation (page, size, title, location);

        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventEntityMapper, times (result.size ()))
                .toDomain(any ());

        verify (eventJpaRepository, times (1))
                .filterEvents(title, location, pageable);
    }


    @Test
    public void shouldGetEventsFilteredByDateSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Date date = new Date (1726472944000L);
        Pageable pageable = PageRequest.of (page, size);
        EventEntity event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Classroom 3"
        );

        EventEntity event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        EventEntity event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponse response = new EventResponse(
                9L,
                "title",
                "description",
                "location",
                date,
                getOrganizerForTest ().getFirstName (),
                List.of (),
                date
        );

        List<EventEntity> events = List.of (event1, event2, event3);
        Page<EventEntity> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventJpaRepository.findByDateBetween(date, pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventEntityMapper.toDomain(any ()))
                .thenReturn (response);

        //When
        List<EventResponse> result = eventService.getEventsByDate (
                page, size, date.getTime ()
        );
        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventEntityMapper, times (result.size ()))
                .toDomain(any ());

        verify (eventJpaRepository, times (1))
                .findByDateBetween(date, pageable);
    }

    private EventEntity getEvent(
            Long eventId,
            String title,
            String description,
            String location
    ) {
        return new EventEntity(
                eventId,
                title,
                description,
                location,
                new Date (1726472944000L),
                new Date (1726172944000L),
                getOrganizerForTest (),
                List.of ()
        );
    }

    private UserEntity getOrganizerForTest() {
        return new UserEntity(
                1L,
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