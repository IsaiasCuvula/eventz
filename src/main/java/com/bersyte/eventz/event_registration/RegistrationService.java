package com.bersyte.eventz.event_registration;

import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventService;
import com.bersyte.eventz.exceptions.EventRegistrationException;
import com.bersyte.eventz.security.auth.AppUser;
import com.bersyte.eventz.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

            final boolean isUserAlreadyRegistered = isUserRegistered(user, event);

            if (isUserAlreadyRegistered) {
                throw new EventRegistrationException("User already registered");
            }

            Registration registration = RegistrationMapper.toEntity(new Date(), event, user);
            final Registration result = registrationRepository.save(registration);
            return RegistrationMapper.toResponseDTO(result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    private boolean isUserRegistered(AppUser user, Event event) {
        final List<Registration> registrations = event.getRegistrations();

        for (Registration registration : registrations) {
            if (registration.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

}
