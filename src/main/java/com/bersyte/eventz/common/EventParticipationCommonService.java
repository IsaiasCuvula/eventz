package com.bersyte.eventz.common;

import com.bersyte.eventz.event_participation.*;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.exceptions.EventRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventParticipationCommonService {

    final private EventParticipationRepository eventParticipationRepository;

    public EventParticipationResponseDto registerUserToEvent(
            AppUser participant,
            Event event
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
                EventParticipation registration = EventParticipationMapper.toEntity(
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
            return EventParticipationMapper.toResponseDTO(result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }
}
