package com.bersyte.eventz.features.notifications.infrastructure.listeners;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.common.infrastructure.utils.SafeActionExecutor;
import com.bersyte.eventz.features.events.application.events.EventCreationEvent;
import com.bersyte.eventz.features.events.application.events.EventUpdatedEvent;
import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import com.bersyte.eventz.features.registrations.domain.model.EventRegistration;
import com.bersyte.eventz.features.registrations.domain.repository.EventRegistrationRepository;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

public class EventsNotificationListener {
    private final NotificationService notificationService;
    private final SafeActionExecutor action;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    
    public EventsNotificationListener(NotificationService notificationService, SafeActionExecutor action, EventRegistrationRepository registrationRepository, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.action = action;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventCreated(EventCreationEvent event){
        int currentPage = 0;
        int pageSize = 100;
        PagedResult<AppUser> usersPaged;
        do {
            Pagination pagination = new Pagination(currentPage, pageSize);
            usersPaged  = userRepository.fetchUsers(pagination);
            List<AppUser> allUsers =  usersPaged.data();
            
            for (var user : allUsers) {
                sendEventCreationEmail(user, event);
            }
            
            currentPage++;
        } while (currentPage <= usersPaged.totalPages());
       
    }
    
    private void sendEventCreationEmail(AppUser user,EventCreationEvent event){
        var body = Map.of(
                "username", user.getFullName(),
                "eventTitle", event.title(),
                "eventDescription", event.description()
        );
        
        action.safeExecute(
                () -> notificationService.sendEmail(
                        user.getEmail(),
                        "New Event Alert: " + event.title(),
                        "new-event-announcement",
                        body
                ),
                "New Event Broadcast",
                user.getEmail()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventUpdated(EventUpdatedEvent event) {
        int currentPage = 0;
        int pageSize = 100;
        PagedResult<EventRegistration> pagedResult;
        do {
            Pagination pagination = new Pagination(currentPage, pageSize);
            pagedResult = registrationRepository.fetchParticipants(event.id(), pagination);
            List<AppUser> participants = pagedResult.data().stream().map(
                    EventRegistration::getUser
            ).toList();
            
            for (var participant : participants){
                sendEventUpdatedEmail(event, participant);
            }
            currentPage++;
        } while (currentPage <= pagedResult.totalPages());
    }
    
    private void sendEventUpdatedEmail(EventUpdatedEvent event, AppUser participant){
        var body = Map.of(
                "attendeeName", participant.getFullName(),
                "eventTitle", event.title(),
                "eventLocation", event.location(),
                "eventDate", event.date().toString()
        );
        
        action.safeExecute(
                () -> notificationService.sendEmail(
                        participant.getEmail(),
                        "Update: " + event.title(),
                        "event-details-update",
                        body
                ),
                "Event Update Notification",
                participant.getEmail()
        );
    }
}
