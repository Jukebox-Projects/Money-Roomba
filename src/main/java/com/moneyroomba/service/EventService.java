package com.moneyroomba.service;

import com.moneyroomba.domain.Event;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.EventRepository;
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

    private final Boolean UNOPENED_STATUS = false;

    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
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

    private void addDestinationPath(List<Event> events) {
        events.forEach(this::appendNotificationEntityData);
    }

    private void appendNotificationEntityData(Event event) {
        String destinationPath = "/";
        String message = "";
        if (event.getSourceEntity().equals(SourceEntity.TRANSACTION)) {
            destinationPath = String.format("transaction/%d/%s", event.getSourceId(), "view");
            message = "incomingTransaction";
        } else if (event.getSourceEntity().equals(SourceEntity.GIFTED_LICENSE)) {
            destinationPath = "license/view";
            message = "giftedLicense";
        } else if (event.getSourceEntity().equals(SourceEntity.LICENSE)) {
            destinationPath = "license/view";
            message = "purchasedLicense";
        }
        event.setDestinationPath(destinationPath);
        event.setMessage(message);
    }
}
