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


import java.util.Map;

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
        var body = Map.of(
                "attendeeName", event.attendeeName(),
                "eventTitle", event.eventTitle(),
                "eventLocation", event.eventLocation(),
                "eventDate", event.eventDate().toString(),
                "checkInToken", event.checkInToken()
        );
        
        
        action.safeExecute(
                () -> notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Event Confirmation: " + event.eventTitle(),
                        "event-registration", //HTML file name
                        body
                ),
                "Join event",
                event.attendeeEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishUpdateCheckinToken(UpdateCheckinTokenEvent event) {
        
        var body = Map.of(
                "eventTitle", event.eventTitle(),
                "newCheckInToken", event.newCheckInToken()
        );
        
        action.safeExecute(
                () ->  notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Event Ticket updated",
                        "update-check-in-token",
                        body
                ),
                "Ticket Update",
                event.attendeeEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishCheckin(CheckinEvent event) {
        var body = Map.of(
                "eventTitle", event.eventTitle(),
                "eventLocation", event.eventLocation(),
                "eventDate", event.checkInDate().toString()
        );
        
        action.safeExecute(
                () ->  notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Ticket for" + event.eventTitle() + "CheckedIn",
                        "check-in",
                        body
                ),
                "Event Checkin",
                event.attendeeEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTicketCancellation(TicketCancellationEvent event) {
        
        var body = Map.of(
                "eventTitle", event.eventTitle(),
                "eventDate", event.eventDate().toString(),
                "status", event.status().name()
        );
        
        action.safeExecute(
                () ->  notificationService.sendEmail(
                        event.attendeeEmail(),
                        "Event " + event.eventTitle() + "Ticket Cancelled",
                        "ticket-cancellation",
                        body
                ),
                "Event Ticket cancellation",
                event.attendeeEmail()
        );
    }
}
