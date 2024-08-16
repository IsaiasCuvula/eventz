package com.bersyte.eventz.event_registration;

import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventService;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.security.auth.AppUser;
import com.bersyte.eventz.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UsersService usersService;
    private final EventService eventService;


    public RegistrationResponseDTO registerToEvent(
            Integer eventId,
            UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();
            AppUser user = usersService.getUserByEmail(email);
            Event event = eventService.findEventById(eventId);
            Registration registration = RegistrationMapper.toEntity(new Date(), event, user);
            final Registration result = registrationRepository.save(registration);
            return RegistrationMapper.toResponseDTO(result);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Failed to register user to the event %s" + e.getLocalizedMessage()
            );
        }
    }

}
