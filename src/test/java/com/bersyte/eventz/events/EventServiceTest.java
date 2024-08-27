package com.bersyte.eventz.events;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.common.UserRole;
import com.bersyte.eventz.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

//    @Test
//    void getAllEvents() {
//    }
//
//    @Test
//    void getUpcomingEvents() {
//    }
//
//    @Test
//    void getEventById() {
//    }
//
//    @Test
//    void updateEvent() {
//    }
//
//    @Test
//    void deleteEvent() {
//    }
//
//    @Test
//    void filterEvents() {
//    }
//
//    @Test
//    void getEventsByDate() {
//    }
}