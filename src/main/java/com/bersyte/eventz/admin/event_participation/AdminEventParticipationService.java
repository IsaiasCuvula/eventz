package com.bersyte.eventz.admin.event_participation;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.EventCommonService;
import com.bersyte.eventz.common.EventParticipationCommonService;
import com.bersyte.eventz.common.UserCommonService;
import com.bersyte.eventz.event_participation.EventParticipationResponseDTO;
import com.bersyte.eventz.events.Event;
import com.bersyte.eventz.exceptions.EventRegistrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminEventParticipationService {
    final private EventParticipationCommonService eventParticipationCommonService;
    final private EventCommonService eventCommonService;
    final private UserCommonService userCommonService;


    public EventParticipationResponseDTO registerUserToEvent(
            Long participantId, Long eventId
    ) {
        try {
            AppUser participant = userCommonService.getUserById(participantId);
            Event event = eventCommonService.findEventById(eventId);
            return eventParticipationCommonService.registerUserToEvent(
                    participant, event
            );
        } catch (EventRegistrationException e) {
            throw new EventRegistrationException(
                    "Failed to register user to the event - " + e.getLocalizedMessage()
            );
        }
    }

    public EventParticipationResponseDTO removeParticipantFromEvent(Long participantId, Long eventId) {
        return null;
    }
}
