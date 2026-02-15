package com.bersyte.eventz.features.notifications.infrastructure.listeners;

import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import com.bersyte.eventz.features.registrations.application.events.CheckinEvent;
import com.bersyte.eventz.features.registrations.application.events.EventRegistrationEvent;
import com.bersyte.eventz.features.registrations.application.events.TicketCancellationEvent;
import com.bersyte.eventz.features.registrations.application.events.UpdateCheckinTokenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventRegistrationNotificationListener {
    private static final Logger logger = LoggerFactory.getLogger(EventRegistrationNotificationListener.class);
    
    private final NotificationService notificationService;
    
    public EventRegistrationNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishJoinEvent(EventRegistrationEvent event) {
        try {
            notificationService.sendEmail(
                    event.attendeeEmail(),event.attendeeName(),
                    event.eventTitle() + event.eventLocation() + event.eventDate()
                            + event.eventDescription() + event.status()
            );
        } catch (Exception e) {
            logger.error("Could not send event registration email to {}. Reason: {}", event.attendeeEmail(), e.getMessage());
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishUpdateCheckinToken(UpdateCheckinTokenEvent event) {
        try {
            notificationService.sendEmail(
                    event.attendeeEmail(),
                    "Event Ticket updated",
                    event.eventTitle() + event.newCheckInToken()
            );
        } catch (Exception e) {
            logger.error("Could not send event update checkin token email to {}. Reason: {}", event.attendeeEmail(), e.getMessage());
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishCheckin(CheckinEvent event) {
        try {
            notificationService.sendEmail(
                    event.attendeeEmail(),
                    "Ticket for" + event.eventTitle() + "CheckedIn",
                    event.eventTitle() + event.eventLocation() + event.checkInDate()
            );
        } catch (Exception e) {
            logger.error("Could not send event checkin email to {}. Reason: {}", event.attendeeEmail(), e.getMessage());
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTicketCancellation(TicketCancellationEvent event) {
        try {
            notificationService.sendEmail(
                    event.attendeeEmail(),
                    "Event " + event.eventTitle() + "Ticket Cancelled",
                    event.eventTitle() + event.eventDate()
                            + event.eventDescription() + event.status()
            );
        } catch (Exception e) {
            logger.error("Could not send ticket cancellation email to {}. Reason: {}", event.attendeeEmail(), e.getMessage());
        }
    }
}
