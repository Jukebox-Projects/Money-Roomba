package com.moneyroomba.service;

import com.moneyroomba.domain.Event;
import com.moneyroomba.domain.Notification;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.EventRepository;
import com.moneyroomba.repository.NotificationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Event}.
 */
@Service
@Transactional
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final UserService userService;

    private final NotificationRepository notificationRepository;

    private final Boolean UNOPENED_STATUS = false;

    public EventService(EventRepository eventRepository, UserService userService, NotificationRepository notificationRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Save a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    /**
     * Partially update a event.
     *
     * @param event the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Event> partialUpdate(Event event) {
        log.debug("Request to partially update Event : {}", event);

        return eventRepository
            .findById(event.getId())
            .map(
                existingEvent -> {
                    if (event.getEventType() != null) {
                        existingEvent.setEventType(event.getEventType());
                    }
                    if (event.getDateAdded() != null) {
                        existingEvent.setDateAdded(event.getDateAdded());
                    }
                    if (event.getSourceId() != null) {
                        existingEvent.setSourceId(event.getSourceId());
                    }
                    if (event.getSourceEntity() != null) {
                        existingEvent.setSourceEntity(event.getSourceEntity());
                    }
                    if (event.getUserName() != null) {
                        existingEvent.setUserName(event.getUserName());
                    }
                    if (event.getUserLastName() != null) {
                        existingEvent.setUserLastName(event.getUserLastName());
                    }

                    return existingEvent;
                }
            )
            .map(eventRepository::save);
    }

    /**
     * Get all the events.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        log.debug("Request to get all Events");
        return eventRepository.findAll();
    }

    /**
     * Get all the events join notifications where notifications are not opened.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Event> findAllNotificationsNotOpened() {
        log.debug("Request to get all unopened notifications");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<Event> results = eventRepository.findAllByNotificationStatus(user.get().getId(), UNOPENED_STATUS);
        if (!results.isEmpty()) {
            addDestinationPath(results);
        }
        return results;
    }

    /**
     * Get all the events join notifications
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Event> findAllNotificationsByUser() {
        log.debug("Request to get all unopened notifications");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<Event> results = eventRepository.findAllByUserOrderByDateAddedDesc(user.get());
        if (!results.isEmpty()) {
            addDestinationPath(results);
        }
        return results;
    }

    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }

    /**
     * Save a event conected with a notification.
     *
     * @param eventType the type of action done.
     * @param sourceEntity the source entity of the event.
     * @param userDetails data of the user performing the action.
     */
    public Event createEventAndNotification(EventType eventType, Long sourceId, SourceEntity sourceEntity, UserDetails userDetails) {
        log.debug("Request to save a Notification and an Event");
        Notification notification = new Notification(LocalDate.now(), false);
        Notification savedNotification = notificationRepository.save(notification);

        Event event = new Event(
            eventType,
            LocalDate.now(),
            sourceId,
            sourceEntity,
            userDetails.getInternalUser().getFirstName(),
            userDetails.getInternalUser().getLastName(),
            savedNotification,
            userDetails
        );
        return eventRepository.save(event);
    }

    private void addDestinationPath(List<Event> events) {
        events.forEach(this::appendNotificationEntityData);
    }

    private void appendNotificationEntityData(Event event) {
        String destinationPath = "/";
        String message = "";
        if (event.getEventType().equals(EventType.TRANSCTION_RECEIVED)) {
            destinationPath = String.format("transaction/%d/%s", event.getSourceId(), "view");
            message = "incomingTransaction";
        } else if (event.getEventType().equals(EventType.POSSIBLE_TRANSACTION_ADDED_EMAIL)) {
            destinationPath = String.format("transaction/%d/%s", event.getSourceId(), "view");
            message = "emailTransaction";
        } else if (event.getEventType().equals(EventType.POSSIBLE_TRANSACTION_ADDED_EMAIL)) {
            destinationPath = String.format("transaction/%d/%s", event.getSourceId(), "view");
            message = "emailTransaction";
        } else if (event.getEventType().equals(EventType.LICENSE_GIFTED)) {
            destinationPath = "license/view";
            message = "giftedLicense";
        } else if (event.getEventType().equals(EventType.LICENSE_PURCHASED)) {
            destinationPath = "license/view";
            message = "purchasedLicense";
        } else if (event.getEventType().equals(EventType.INVALID_ATTACHMENT)) {
            destinationPath = "";
            message = "invalidAttachment";
        } else if (event.getEventType().equals(EventType.IMPORT_LIMIT_REACHED)) {
            destinationPath = "license/view";
            message = "importLimitReached";
        }
        event.setDestinationPath(destinationPath);
        event.setMessage(message);
    }
}
