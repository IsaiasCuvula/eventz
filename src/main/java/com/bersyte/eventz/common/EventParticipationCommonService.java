package com.bersyte.eventz.common;

import com.bersyte.eventz.features.event_participation.*;
import com.bersyte.eventz.features.events.EventEntity;
import com.bersyte.eventz.exceptions.EventRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EventParticipationCommonService {

    private final EventParticipationRepository eventParticipationRepository;
    private final EventParticipationMapper eventParticipationMapper;

    public EventParticipationCommonService(EventParticipationRepository eventParticipationRepository, EventParticipationMapper eventParticipationMapper) {
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventParticipationMapper = eventParticipationMapper;
    }

    public EventParticipationResponseDto registerUserToEvent(
            AppUser participant,
            EventEntity event
    ) {
        try {
            final Date updatedDate = new Date();
            final Date createdDate = new Date();

            Optional<EventParticipation> existingRegistration =
                    eventParticipationRepository.findByUserIdAndEventId(
                            participant.getId(),
                            event.getId()
                    );

            if (existingRegistration.isEmpty()) {
                EventParticipation registration = eventParticipationMapper.toEntity (
                        createdDate,
                        updatedDate,
                        event,
                        participant
                );
                registration.setStatus(ParticipationStatus.ACTIVE);
                return saveRegistration(registration);
            }

            final EventParticipation oldRegistration = existingRegistration.get();

            if (oldRegistration.getStatus() == ParticipationStatus.ACTIVE) {
                throw new EventRegistrationException("User already registered");
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


    public EventParticipationResponseDto saveRegistration(EventParticipation registration) {
        try {
            final EventParticipation result = eventParticipationRepository.save(registration);
            return eventParticipationMapper.toResponseDTO (result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }
}
