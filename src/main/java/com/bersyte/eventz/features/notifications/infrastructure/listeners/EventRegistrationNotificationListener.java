package com.bersyte.eventz.features.notifications.infrastructure.listeners;

import com.bersyte.eventz.common.infrastructure.utils.SafeActionExecutor;
import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import com.bersyte.eventz.features.registrations.application.events.CheckinEvent;
import com.bersyte.eventz.features.registrations.application.events.EventRegistrationEvent;
import com.bersyte.eventz.features.registrations.application.events.TicketCancellationEvent;
import com.bersyte.eventz.features.registrations.application.events.UpdateCheckinTokenEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventRegistrationNotificationListener {
    private final NotificationService notificationService;
    private final SafeActionExecutor action;
    
    public EventRegistrationNotificationListener(NotificationService notificationService, SafeActionExecutor action) {
        this.notificationService = notificationService;
        this.action = action;
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishJoinEvent(EventRegistrationEvent event) {
        action.safeExecute(
                () -> notificationService.sendEmail(
                        event.attendeeEmail(),event.attendeeName(),
                        event.eventTitle() + event.eventLocation() + event.eventDate()
                                + event.eventDescription() + event.status()
                ),
                "Join event",
                event.attendeeEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishUpdateCheckinToken(UpdateCheckinTokenEvent event) {
        action.safeExecute(
                () ->  notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Event Ticket updated",
                        event.eventTitle() + event.newCheckInToken()
                ),
                "Ticket Update",
                event.attendeeEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishCheckin(CheckinEvent event) {
        action.safeExecute(
                () ->  notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Ticket for" + event.eventTitle() + "CheckedIn",
                        event.eventTitle() + event.eventLocation() + event.checkInDate()
                ),
                "Event Checkin",
                event.attendeeEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTicketCancellation(TicketCancellationEvent event) {
        action.safeExecute(
                () ->  notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Event " + event.eventTitle() + "Ticket Cancelled",
                        event.eventTitle() + event.eventDate()
                                + event.eventDescription() + event.status()
                ),
                "Event Ticket cancellation",
                event.attendeeEmail()
        );
    }
}
