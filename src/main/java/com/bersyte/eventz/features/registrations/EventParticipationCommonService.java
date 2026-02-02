package com.bersyte.eventz.features.registrations;

import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.common.presentation.exceptions.EventRegistrationException;
import com.bersyte.eventz.features.registrations.application.dtos.TicketResponse;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.entities.EventRegistrationEntity;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.mappers.EventRegistrationEntityMapper;
import com.bersyte.eventz.features.registrations.infrastructure.persistence.repository.EventRegistrationJpaRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EventParticipationCommonService {

    private final EventRegistrationJpaRepository eventParticipationJpaRepository;
    private final EventRegistrationEntityMapper eventParticipationMapper;

    public EventParticipationCommonService(EventRegistrationJpaRepository eventParticipationJpaRepository, EventRegistrationEntityMapper eventParticipationMapper) {
        this.eventParticipationJpaRepository = eventParticipationJpaRepository;
        this.eventParticipationMapper = eventParticipationMapper;
    }

    public TicketResponse registerUserToEvent(
            UserEntity participant,
            EventEntity event
    ) {
        try {
            final Date updatedDate = new Date();
            final Date createdDate = new Date();

            Optional<EventRegistrationEntity> existingRegistration =
                    eventParticipationJpaRepository.findByUserIdAndEventId(
                            participant.getId(),
                            event.getId()
                    );

            if (existingRegistration.isEmpty()) {
                EventRegistrationEntity registration = eventParticipationMapper.toEntity (
                        createdDate,
                        updatedDate,
                        event,
                        participant
                );
                registration.setStatus(RegistrationStatus.ACTIVE);
                return saveRegistration(registration);
            }

            final EventRegistrationEntity oldRegistration = existingRegistration.get();

            if (oldRegistration.getStatus() == RegistrationStatus.ACTIVE) {
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


    public TicketResponse saveRegistration(EventRegistrationEntity registration) {
        try {
            final EventRegistrationEntity result = eventParticipationJpaRepository.save(registration);
            return eventParticipationMapper.toResponseDTO (result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }
}
