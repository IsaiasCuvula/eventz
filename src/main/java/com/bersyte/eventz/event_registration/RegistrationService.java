package com.bersyte.eventz.event_registration;

import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.events.EventService;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.exceptions.EventRegistrationException;
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

    public String cancelRegistration(
            UserDetails userDetails,
            Long eventId
    ) {
        try {
            AppUser user = this.getUserByEmail(userDetails);
            Event event = eventService.findEventById(eventId);

            final boolean isUserAlreadyRegistered = isUserRegistered(user.getId(), event.getId());

            if (!isUserAlreadyRegistered) {
                throw new EventRegistrationException("User not registered in this event");
            }

            registrationRepository.updateRegistrationStatus(
                    user.getId(), eventId, RegistrationStatus.CANCELED
            );
            return "Event Registration Cancelled Successfully";
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }
    }
    //TODO- EVENT CREATOR AND ADMIN CAN REMOVE PARTICIPANTS


    //
    public RegistrationResponseDTO registerUserToEvent(
            Long eventId,
            UserDetails userDetails
    ) {
        try {
            AppUser user = this.getUserByEmail(userDetails);
            Event event = eventService.findEventById(eventId);

            final boolean isUserAlreadyRegistered = isUserRegistered(user.getId(), event.getId());

            if (isUserAlreadyRegistered) {
                throw new EventRegistrationException("User already registered");
            }

            Registration registration = RegistrationMapper.toEntity(new Date(), event, user);
            registration.setStatus(RegistrationStatus.ACTIVE);
            //
            final Registration result = registrationRepository.save(registration);
            return RegistrationMapper.toResponseDTO(result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    private boolean isUserRegistered(Long userId, Long eventId) {
        try {
            return registrationRepository.existsByUserIdAndEventId(userId, eventId);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to check if user is registered to the event - " + e.getLocalizedMessage()
            );
        }
    }

    private AppUser getUserByEmail(UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            return usersService.getUserByEmail(email);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Error getting user " + e.getLocalizedMessage()
            );
        }
    }

}
