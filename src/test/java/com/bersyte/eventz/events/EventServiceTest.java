package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.users.UserService;
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
    private EventRepository eventRepository;
    @Mock
    private UserService usersService;
    @Mock
    private EventCommonService eventCommonService;
    @Mock
    private EventMappers eventMappers;
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
        AppUser organizer = getOrganizerForTest ();

        EventRequestDto dto = new EventRequestDto (
                title,
                description,
                location,
                eventDate,
                createdAt
        );

        Event event = new Event (
                2L,
                title,
                description,
                location,
                new Date (1726472944000L),
                new Date (1721472944000L),
                organizer,
                List.of ()
        );

        Event savedEvent = new Event (
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

        Mockito.when (eventMappers.toEventEntity (dto))
                .thenReturn (event);

        Mockito.when (eventRepository.save (event))
                .thenReturn (savedEvent);

        Mockito.when (eventMappers.toResponseDTO (savedEvent))
                .thenReturn (
                        new EventResponseDto (
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
        EventResponseDto responseDto = eventService.createEvent (dto, userDetails);

        //Assert
        assertNotNull (responseDto);

        assertEquals (dto.title (), responseDto.title ());

        verify (eventMappers, times (1))
                .toEventEntity (dto);

        verify (eventRepository, times (1))
                .save (event);

        verify (eventMappers, times (1))
                .toResponseDTO (savedEvent);

    }

    @Test
    void shouldGetAllEventsSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of (page, size);
        Event event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        Event event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        Event event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponseDto response = new EventResponseDto (
                9L,
                "title",
                "description",
                "location",
                new Date (1726472944000L),
                getOrganizerForTest ().getFirstName (),
                List.of (),
                new Date (1726172944000L)
        );

        List<Event> events = List.of (event1, event2, event3);
        Page<Event> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventRepository.findAll (pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventMappers.toResponseDTO (any ()))
                .thenReturn (response);

        //When
        List<EventResponseDto> result = eventService.getAllEvents (page, size);

        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventMappers, times (result.size ()))
                .toResponseDTO (any ());

        verify (eventRepository, times (1))
                .findAll (pageable);
    }


    @Test
    void shouldGetAllUpcomingEventsSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Date date = new Date (1724831344000L);
        Pageable pageable = PageRequest.of (page, size);
        Event event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        Event event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        Event event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponseDto response = new EventResponseDto (
                9L,
                "title",
                "description",
                "location",
                date,
                getOrganizerForTest ().getFirstName (),
                List.of (),
                new Date (1726172944000L)
        );

        List<Event> events = List.of (event1, event2, event3);
        Page<Event> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventRepository.findUpcomingEvents (date, pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventMappers.toResponseDTO (any ()))
                .thenReturn (response);

        //When
        List<EventResponseDto> result =
                eventService.getUpcomingEvents (date, page, size);

        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventMappers, times (result.size ()))
                .toResponseDTO (any ());

        verify (eventRepository, times (1))
                .findUpcomingEvents (date, pageable);
    }

    @Test
    public void shouldGetEventByIdSuccessfully() {
        //Arrange
        Long eventId = 4L;

        Event event1 = getEvent (
                eventId,
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3"
        );

        EventResponseDto response = new EventResponseDto (
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

        Mockito.when (eventMappers.toResponseDTO (event1))
                .thenReturn (response);

        //When
        EventResponseDto result = eventService.getEventById (eventId);

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

        EventRequestDto dto = new EventRequestDto (
                title,
                description,
                location,
                eventDate.getTime (),
                createdAt.getTime ()
        );

        Event event = getEvent (
                eventId,
                title,
                description,
                location
        );

        EventResponseDto response = new EventResponseDto (
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
        EventResponseDto result = eventService.updateEvent (eventId, dto, userDetails);

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

        EventRequestDto dto = new EventRequestDto (
                "Training Workshop",
                "Training session on new software tools",
                "Training Room 3",
                new Date (1726472944000L).getTime (),
                new Date (1721472944000L).getTime ()
        );

        Event event = getEvent (
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
        verify (eventRepository, never ()).delete (event);
    }

    @Test
    void shouldDeleteEventSuccessfully() {
        //Arrange
        String email = "isaias@gmail.com";
        Long eventId = 4L;
        Event event = getEvent (
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
        verify (eventRepository, times (1))
                .delete (event);
    }

    @Test
    public void shouldThrowAccessDeniedExceptionWhenUserNotAuthorizedToDeleteEvent() {
        //Arrange
        Long eventId = 4L;
        String expectedMsg = "You do not have permission to delete this event";
        String email = "bernardo@gmail.com";
        Event event = getEvent (
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

        verify (eventRepository, never ()).delete (event);
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
        Event event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Classroom 3"
        );

        Event event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        Event event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponseDto response = new EventResponseDto (
                9L,
                "title",
                "description",
                "location",
                date,
                getOrganizerForTest ().getFirstName (),
                List.of (),
                date
        );

        List<Event> events = List.of (event1, event2, event3);
        Page<Event> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventRepository.filterEventsByTitleAndLocation (title, location, pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventMappers.toResponseDTO (any ()))
                .thenReturn (response);

        //When
        List<EventResponseDto> result =
                eventService.getFilteredEventsByTitleAndLocation (page, size, title, location);

        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventMappers, times (result.size ()))
                .toResponseDTO (any ());

        verify (eventRepository, times (1))
                .filterEventsByTitleAndLocation (title, location, pageable);
    }


    @Test
    public void shouldGetEventsFilteredByDateSuccessfully() {
        //Arrange
        int page = 0;
        int size = 10;
        Date date = new Date (1726472944000L);
        Pageable pageable = PageRequest.of (page, size);
        Event event1 = getEvent (
                4L,
                "Training Workshop",
                "Training session on new software tools",
                "Classroom 3"
        );

        Event event2 = getEvent (
                5L,
                "Math Training",
                "Math training session on new software tools",
                "Classroom 5"
        );

        Event event3 = getEvent (
                8L,
                "Medical Workshop",
                "Medical session on new-hires tools",
                "Room 89"
        );

        EventResponseDto response = new EventResponseDto (
                9L,
                "title",
                "description",
                "location",
                date,
                getOrganizerForTest ().getFirstName (),
                List.of (),
                date
        );

        List<Event> events = List.of (event1, event2, event3);
        Page<Event> eventsPage = new PageImpl<> (events);

        //Mock calls
        Mockito.when (eventRepository.findEventsByDate (date, pageable))
                .thenReturn (eventsPage);

        Mockito.when (eventMappers.toResponseDTO (any ()))
                .thenReturn (response);

        //When
        List<EventResponseDto> result = eventService.getEventsByDate (
                page, size, date.getTime ()
        );
        //Act - Assert
        assertNotNull (result);
        assertEquals (events.size (), result.size ());
        verify (eventMappers, times (result.size ()))
                .toResponseDTO (any ());

        verify (eventRepository, times (1))
                .findEventsByDate (date, pageable);
    }

    private Event getEvent(
            Long eventId,
            String title,
            String description,
            String location
    ) {
        return new Event (
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

    private AppUser getOrganizerForTest() {
        return new AppUser (
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