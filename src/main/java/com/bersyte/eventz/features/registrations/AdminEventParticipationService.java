package com.bersyte.eventz.features.registrations;

import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.mappers.EventRegistrationEntityMapper;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.repository.EventParticipationJpaRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.event_participation.*;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.common.presentation.exceptions.EventRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class AdminEventParticipationService {
    final private EventParticipationCommonService eventParticipationCommonService;
    final private EventParticipationJpaRepository eventParticipationJpaRepository;
    final private EventCommonService eventCommonService;
    final private UserCommonService userCommonService;
    final private EventRegistrationEntityMapper eventParticipationMapper;

    public AdminEventParticipationService(EventParticipationCommonService eventParticipationCommonService, EventParticipationJpaRepository eventParticipationJpaRepository, EventCommonService eventCommonService, UserCommonService userCommonService, EventRegistrationEntityMapper eventParticipationMapper) {
        this.eventParticipationCommonService = eventParticipationCommonService;
        this.eventParticipationJpaRepository = eventParticipationJpaRepository;
        this.eventCommonService = eventCommonService;
        this.userCommonService = userCommonService;
        this.eventParticipationMapper = eventParticipationMapper;
    }
 
    
    public TicketResponse registerUserToEvent(
            Long participantId, Long eventId
    ) {
        try {
            UserEntity participant = getUserById(participantId);
            EventEntity event = getEventById(eventId);
            return eventParticipationCommonService.registerUserToEvent(
                    participant, event
            );
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    public TicketResponse removeParticipantFromEvent(
            Long participantId, Long eventId
    ) {
        try {
            UserEntity participant = getUserById(participantId);
            EventEntity event = getEventById(eventId);

            Optional<EventRegistrationEntity> registration =
                    eventParticipationJpaRepository.findByUserIdAndEventId(
                            participant.getId(), event.getId()
                    );

            if (registration.isEmpty()) {
                throw new EventRegistrationException("EventEntity with eventId " + eventId + " does not exist");
            }

            EventRegistrationEntity existingRegistration = registration.get();

            if (existingRegistration.getStatus() == RegistrationStatus.CANCELED) {
                throw new EventRegistrationException("EventEntity with eventId " + eventId + " has already been canceled");
            }

            existingRegistration.setStatus(RegistrationStatus.CANCELED);
            existingRegistration.setUpdateAt(new Date());
            final EventRegistrationEntity result = eventParticipationJpaRepository.save(existingRegistration);
            return eventParticipationMapper.toResponseDTO (result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }
    }

    private UserEntity getUserById(Long userId) {
        return userCommonService.getUserById(userId);
    }

    private EventEntity getEventById(Long eventId) {
        return eventCommonService.findEventById(eventId);
    }
}
