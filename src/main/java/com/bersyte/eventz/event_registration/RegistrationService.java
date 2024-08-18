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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UsersService usersService;
    private final EventService eventService;

    //TODO- EVENT CREATOR AND ADMIN CAN REMOVE PARTICIPANTS

    private static Registration getRegistration(Long participantId, Optional<Registration> registration) {

        if (registration.isEmpty()) {
            throw new EventRegistrationException("Registration not found");
        }

        Registration organizerRegistration = registration.get();

        Long eventParticipantId = organizerRegistration.user.getId();

        if (!Objects.equals(eventParticipantId, participantId)) {
            throw new EventRegistrationException("This user is not a participant of the event");
        }

        if (organizerRegistration.getStatus() == RegistrationStatus.CANCELED) {
            throw new EventRegistrationException("This user is not a participant of the event");
        }

        organizerRegistration.setStatus(RegistrationStatus.CANCELED);
        organizerRegistration.setUpdateAt(new Date());
        return organizerRegistration;
    }

    public RegistrationResponseDTO removeParticipantFromEvent(
            UserDetails userDetails, Long participantId, Long eventId
    ) {
        try {
            AppUser currentUser = this.getUserByEmail(userDetails);
            Event event = eventService.findEventById(eventId);
            Long organizerId = event.getOrganizer().getId();

            if (!organizerId.equals(currentUser.getId())) {
                throw new EventRegistrationException("You are not the organizer of the event");
            }

            Optional<Registration> registration = registrationRepository.findByUserIdAndEventId(
                    currentUser.getId(), event.getId()
            );

            Registration organizerRegistration = getRegistration(participantId, registration);

            return this.saveRegistration(organizerRegistration);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }

    }

    //
    public String cancelRegistration(
            UserDetails userDetails,
            Long eventId
    ) {
        try {
            AppUser user = this.getUserByEmail(userDetails);
            Event event = eventService.findEventById(eventId);

            Optional<Registration> registration = registrationRepository.findByUserIdAndEventId(
                    user.getId(),
                    event.getId()
            );

            if (registration.isEmpty()) {
                throw new EventRegistrationException("User not registered in this event");
            }

            final Registration existingRegistration = registration.get();

            if (existingRegistration.status == RegistrationStatus.CANCELED) {
                throw new EventRegistrationException("User not registered in this event");
            }

            registrationRepository.updateRegistrationStatus(
                    user.getId(), eventId,
                    RegistrationStatus.CANCELED,
                    new Date()
            );

            return "Event Registration Cancelled Successfully";


        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }
    }

    public RegistrationResponseDTO registerUserToEvent(
            Long eventId,
            UserDetails userDetails
    ) {
        try {
            AppUser user = this.getUserByEmail(userDetails);
            Event event = eventService.findEventById(eventId);
            final Date updatedDate = new Date();
            final Date createdDate = new Date();

            Optional<Registration> existingRegistration = registrationRepository.findByUserIdAndEventId(
                    user.getId(),
                    event.getId()
            );

            if (existingRegistration.isEmpty()) {
                Registration registration = RegistrationMapper.toEntity(
                        createdDate,
                        updatedDate,
                        event,
                        user
                );
                registration.setStatus(RegistrationStatus.ACTIVE);
                return saveRegistration(registration);
            }

            final Registration oldRegistration = existingRegistration.get();

            if (oldRegistration.status == RegistrationStatus.ACTIVE) {
                throw new EventRegistrationException("User already registered");
            }

            oldRegistration.setStatus(RegistrationStatus.ACTIVE);
            oldRegistration.setUpdateAt(updatedDate);
            return saveRegistration(oldRegistration);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    private RegistrationResponseDTO saveRegistration(Registration registration) {
        try {
            final Registration result = registrationRepository.save(registration);
            return RegistrationMapper.toResponseDTO(result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
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
