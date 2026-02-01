package com.bersyte.eventz.features.registrations;

import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.mappers.EventRegistrationEntityMapper;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.repository.EventParticipationJpaRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.common.presentation.exceptions.DatabaseOperationException;
import com.bersyte.eventz.common.presentation.exceptions.EventRegistrationException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventParticipationService {
    final private EventParticipationCommonService eventParticipationCommonService;
    private final EventParticipationJpaRepository registrationRepository;
    private final EventCommonService eventCommonService;
    private final UserCommonService userCommonService;
    private final EventRegistrationEntityMapper eventParticipationMapper;

    public EventParticipationService(EventParticipationCommonService eventParticipationCommonService, EventParticipationJpaRepository registrationRepository, EventCommonService eventCommonService, UserCommonService userCommonService, EventRegistrationEntityMapper eventParticipationMapper) {
        this.eventParticipationCommonService = eventParticipationCommonService;
        this.registrationRepository = registrationRepository;
        this.eventCommonService = eventCommonService;
        this.userCommonService = userCommonService;
        this.eventParticipationMapper = eventParticipationMapper;
    }

    private static EventRegistrationEntity getRegistration(
            Long participantId,
            Optional<EventRegistrationEntity> registration
    ) {

        if (registration.isEmpty()) {
            throw new EventRegistrationException("Registration not found");
        }

        EventRegistrationEntity organizerRegistration = registration.get();

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

    public TicketResponse organizerAddUserToHisEvent(
            UserDetails organizerDetails,
            Long participantId,
            Long eventId
    ) {
        try {
            UserEntity organizer = this.getUserByEmail(organizerDetails);
            EventEntity event = this.getEvent(eventId);
            Long organizerId = organizer.getId();
            Long eventOrganizerId = event.getOrganizer().getId();
            final Date updatedDate = new Date();
            final Date createdDate = new Date();

            if (!organizerId.equals(eventOrganizerId)) {
                throw new EventRegistrationException("You are not the organizer of this event");
            }

            Optional<EventRegistrationEntity> existingRegistration = getExistingRegistration(
                    participantId, event
            );

            if (existingRegistration.isEmpty()) {
                UserEntity participant = userCommonService.getUserById(participantId);
                EventRegistrationEntity registration = eventParticipationMapper.toEntity (
                        createdDate,
                        updatedDate,
                        event,
                        participant
                );
                registration.setStatus(RegistrationStatus.ACTIVE);
                return saveRegistration(registration);
            }

            EventRegistrationEntity oldRegistration = existingRegistration.get();
            if (oldRegistration.getStatus() == RegistrationStatus.ACTIVE) {
                throw new EventRegistrationException("User is already participating in this event");
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

    public TicketResponse removeParticipantFromEvent(
            UserDetails userDetails, Long participantId, Long eventId
    ) {
        try {
            UserEntity currentUser = this.getUserByEmail(userDetails);
            EventEntity event = this.getEvent(eventId);
            Long organizerId = event.getOrganizer().getId();

            if (!organizerId.equals(currentUser.getId())) {
                throw new EventRegistrationException("You are not the organizer of the event");
            }

            Optional<EventRegistrationEntity> registration = getExistingRegistration(
                    currentUser.getId(), event
            );

            EventRegistrationEntity organizerRegistration = getRegistration(participantId, registration);

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
            UserEntity user = this.getUserByEmail(userDetails);
            EventEntity event = this.getEvent(eventId);

            Optional<EventRegistrationEntity> registration = getExistingRegistration(
                    user.getId(), event
            );

            if (registration.isEmpty()) {
                throw new EventRegistrationException("User not registered in this event");
            }

            final EventRegistrationEntity existingRegistration = registration.get();

            if (existingRegistration.status == RegistrationStatus.CANCELED) {
                throw new EventRegistrationException("User not registered in this event");
            }

            registrationRepository.updateRegistrationStatus(
                    user.getId(), eventId,
                    RegistrationStatus.CANCELED,
                    new Date()
            );

            return "EventEntity Registration Cancelled Successfully";


        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }
    }

    public TicketResponse registerUserToEvent(
            Long eventId,
            UserDetails userDetails
    ) {
        try {
            UserEntity participant = this.getUserByEmail(userDetails);
            EventEntity event = this.getEvent(eventId);
            return eventParticipationCommonService.registerUserToEvent(
                    participant, event
            );
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    private Optional<EventRegistrationEntity> getExistingRegistration(
            Long participantId, EventEntity event
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

    private TicketResponse saveRegistration(EventRegistrationEntity registration) {
        return eventParticipationCommonService.saveRegistration(registration);
    }

    private EventEntity getEvent(Long eventId) {
        try {
            return eventCommonService.findEventById(eventId);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Error getting event " + e.getLocalizedMessage()
            );
        }
    }

    private UserEntity getUserByEmail(UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            return userCommonService.getUserByEmail (email);
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(
                    "Error getting user " + e.getLocalizedMessage()
            );
        }
    }

}
