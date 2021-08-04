package com.moneyroomba.service;

import com.moneyroomba.domain.Event;
import com.moneyroomba.domain.License;
import com.moneyroomba.domain.User;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.EventRepository;
import com.moneyroomba.repository.LicenseRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link License}.
 */
@Service
@Transactional
public class LicenseService {

    private final Logger log = LoggerFactory.getLogger(LicenseService.class);

    private final LicenseRepository licenseRepository;

    private final UserService userService;

    private static final String ENTITY_NAME = "license";

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public LicenseService(
        LicenseRepository licenseRepository,
        UserService userService,
        EventRepository eventRepository,
        UserRepository userRepository
    ) {
        this.licenseRepository = licenseRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a license.
     *
     * @param license the entity to save.
     * @return the persisted entity.
     */
    public License save(License license) {
        log.debug("Request to save License : {}", license);
        createEvent(EventType.CREATE);
        return licenseRepository.save(license);
    }

    /**
     * Partially update a license.
     *
     * @param license the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<License> partialUpdate(License license) {
        log.debug("Request to partially update License : {}", license);
        createEvent(EventType.UPDATE);
        return licenseRepository
            .findById(license.getId())
            .map(
                existingLicense -> {
                    if (license.getCode() != null) {
                        existingLicense.setCode(license.getCode());
                    }
                    if (license.getIsAssigned() != null) {
                        existingLicense.setIsAssigned(license.getIsAssigned());
                    }
                    if (license.getIsActive() != null) {
                        existingLicense.setIsActive(license.getIsActive());
                    }
                    if (license.getCreateMethod() != null) {
                        existingLicense.setCreateMethod(license.getCreateMethod());
                    }

                    return existingLicense;
                }
            )
            .map(licenseRepository::save);
    }

    /**
     * Get all the licenses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<License> findAll() {
        log.debug("Request to get all Licenses");
        return licenseRepository.findAll();
    }

    /**
     * Get one license by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<License> findOne(Long id) {
        log.debug("Request to get License : {}", id);
        return licenseRepository.findById(id);
    }

    public Optional<License> findOne(UUID code) {
        log.debug("Request to get License : {}", code.toString());
        return licenseRepository.findOneByCode(code);
    }

    public License activate(License license) {
        log.debug("Request to activate License : {}", license);
        license.setIsAssigned(true);

        userService.updateUser(license.getCode().toString());

        return licenseRepository.save(license);
    }

    /**
     * Delete the license by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        Optional<License> license = findOne(id);
        if (license.get() != null) {
            if (!license.get().getIsAssigned()) {
                log.debug("Request to delete License : {}", id);
                createEvent(EventType.DELETE);
                licenseRepository.deleteById(id);
            }
        }
    }

    /**
     * Create event.
     *
     * @param eventType of the entity.
     */
    public void createEvent(EventType eventType) {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
        );

        Event event = new Event();
        event.setEventType(eventType);
        event.setDateAdded(LocalDate.now());
        event.setSourceId(user.get().getId());
        event.setSourceEntity(SourceEntity.LICENSE);
        event.setUserName(user.get().getFirstName());
        event.setUserLastName(user.get().getLastName());
        System.out.println(event);
        eventRepository.save(event);
    }
}
