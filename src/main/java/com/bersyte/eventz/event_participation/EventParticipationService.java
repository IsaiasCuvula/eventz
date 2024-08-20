package com.bersyte.eventz.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.common.EventParticipationCommonService;
import com.bersyte.eventz.common.UserCommonService;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import com.bersyte.eventz.exceptions.EventRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventParticipationService {
    final private EventParticipationCommonService eventParticipationCommonService;
    private final EventParticipationRepository registrationRepository;
    private final EventCommonService eventCommonService;
    private final UserCommonService userCommonService;

    private static EventParticipation getRegistration(
            Long participantId,
            Optional<EventParticipation> registration
    ) {

        if (registration.isEmpty()) {
            throw new EventRegistrationException("Registration not found");
        }

        EventParticipation organizerRegistration = registration.get();

        Long eventParticipantId = organizerRegistration.user.getId();

        if (!Objects.equals(eventParticipantId, participantId)) {
            throw new EventRegistrationException("This user is not a participant of the event");
        }

        if (organizerRegistration.getStatus() == ParticipationStatus.CANCELED) {
            throw new EventRegistrationException("This user is not a participant of the event");
        }

        organizerRegistration.setStatus(ParticipationStatus.CANCELED);
        organizerRegistration.setUpdateAt(new Date());
        return organizerRegistration;
    }

    public EventParticipationResponseDto organizerAddUserToHisEvent(
            UserDetails organizerDetails,
            Long participantId,
            Long eventId
    ) {
        try {
            AppUser organizer = this.getUserByEmail(organizerDetails);
            Event event = this.getEvent(eventId);
            Long organizerId = organizer.getId();
            Long eventOrganizerId = event.getOrganizer().getId();
            final Date updatedDate = new Date();
            final Date createdDate = new Date();

            if (!organizerId.equals(eventOrganizerId)) {
                throw new EventRegistrationException("You are not the organizer of this event");
            }

            Optional<EventParticipation> existingRegistration = getExistingRegistration(
                    participantId, event
            );

            if (existingRegistration.isEmpty()) {
                AppUser participant = userCommonService.getUserById(participantId);
                EventParticipation registration = EventParticipationMapper.toEntity(
                        createdDate,
                        updatedDate,
                        event,
                        participant
                );
                registration.setStatus(ParticipationStatus.ACTIVE);
                return saveRegistration(registration);
            }

            EventParticipation oldRegistration = existingRegistration.get();
            if (oldRegistration.getStatus() == ParticipationStatus.ACTIVE) {
                throw new EventRegistrationException("User is already participating in this event");
            }

            oldRegistration.setStatus(ParticipationStatus.ACTIVE);
            oldRegistration.setUpdateAt(updatedDate);
            return saveRegistration(oldRegistration);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    public EventParticipationResponseDto removeParticipantFromEvent(
            UserDetails userDetails, Long participantId, Long eventId
    ) {
        try {
            AppUser currentUser = this.getUserByEmail(userDetails);
            Event event = this.getEvent(eventId);
            Long organizerId = event.getOrganizer().getId();

            if (!organizerId.equals(currentUser.getId())) {
                throw new EventRegistrationException("You are not the organizer of the event");
            }

            Optional<EventParticipation> registration = getExistingRegistration(
                    currentUser.getId(), event
            );

            EventParticipation organizerRegistration = getRegistration(participantId, registration);

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
            Event event = this.getEvent(eventId);

            Optional<EventParticipation> registration = getExistingRegistration(
                    user.getId(), event
            );

            if (registration.isEmpty()) {
                throw new EventRegistrationException("User not registered in this event");
            }

            final EventParticipation existingRegistration = registration.get();

            if (existingRegistration.status == ParticipationStatus.CANCELED) {
                throw new EventRegistrationException("User not registered in this event");
            }

            registrationRepository.updateRegistrationStatus(
                    user.getId(), eventId,
                    ParticipationStatus.CANCELED,
                    new Date()
            );

            return "Event Registration Cancelled Successfully";


        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }
    }

    public EventParticipationResponseDto registerUserToEvent(
            Long eventId,
            UserDetails userDetails
    ) {
        try {
            AppUser participant = this.getUserByEmail(userDetails);
            Event event = this.getEvent(eventId);
            return eventParticipationCommonService.registerUserToEvent(
                    participant, event
            );
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    private Optional<EventParticipation> getExistingRegistration(
            Long participantId, Event event
    ) {
        try {
            return registrationRepository.findByUserIdAndEventId(
                    participantId,
                    event.getId()
            );
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to get event registration - " + e.getLocalizedMessage()
            );
        }
    }

    private EventParticipationResponseDto saveRegistration(EventParticipation registration) {
        return eventParticipationCommonService.saveRegistration(registration);
    }

    private Event getEvent(Long eventId) {
        try {
            return eventCommonService.findEventById(eventId);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Error getting event " + e.getLocalizedMessage()
            );
        }
    }

    private AppUser getUserByEmail(UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            return userCommonService.findUserByEmail(email);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Error getting user " + e.getLocalizedMessage()
            );
        }
    }

}
