package com.bersyte.eventz.admin.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.common.EventParticipationCommonService;
import com.bersyte.eventz.common.UserCommonService;
import com.bersyte.eventz.event_participation.*;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.exceptions.EventRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class AdminEventParticipationService {
    final private EventParticipationCommonService eventParticipationCommonService;
    final private EventParticipationRepository eventParticipationRepository;
    final private EventCommonService eventCommonService;
    final private UserCommonService userCommonService;
    final private EventParticipationMapper eventParticipationMapper;

    public AdminEventParticipationService(EventParticipationCommonService eventParticipationCommonService, EventParticipationRepository eventParticipationRepository, EventCommonService eventCommonService, UserCommonService userCommonService, EventParticipationMapper eventParticipationMapper) {
        this.eventParticipationCommonService = eventParticipationCommonService;
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventCommonService = eventCommonService;
        this.userCommonService = userCommonService;
        this.eventParticipationMapper = eventParticipationMapper;
    }


    public EventParticipationResponseDto registerUserToEvent(
            Long participantId, Long eventId
    ) {
        try {
            AppUser participant = getUserById(participantId);
            Event event = getEventById(eventId);
            return eventParticipationCommonService.registerUserToEvent(
                    participant, event
            );
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    public EventParticipationResponseDto removeParticipantFromEvent(
            Long participantId, Long eventId
    ) {
        try {
            AppUser participant = getUserById(participantId);
            Event event = getEventById(eventId);

            Optional<EventParticipation> registration =
                    eventParticipationRepository.findByUserIdAndEventId(
                            participant.getId(), event.getId()
                    );

            if (registration.isEmpty()) {
                throw new EventRegistrationException("Event with id " + eventId + " does not exist");
            }

            EventParticipation existingRegistration = registration.get();

            if (existingRegistration.getStatus() == ParticipationStatus.CANCELED) {
                throw new EventRegistrationException("Event with id " + eventId + " has already been canceled");
            }

            existingRegistration.setStatus(ParticipationStatus.CANCELED);
            existingRegistration.setUpdateAt(new Date());
            final EventParticipation result = eventParticipationRepository.save(existingRegistration);
            return eventParticipationMapper.toResponseDTO (result);
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to cancel registration from the event - " + e.getLocalizedMessage()
            );
        }
    }

    private AppUser getUserById(Long userId) {
        return userCommonService.getUserById(userId);
    }

    private Event getEventById(Long eventId) {
        return eventCommonService.findEventById(eventId);
    }
}
