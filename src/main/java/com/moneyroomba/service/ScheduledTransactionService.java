package com.moneyroomba.service;

import com.moneyroomba.domain.*;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.EventRepository;
import com.moneyroomba.repository.ScheduledTransactionRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ScheduledTransaction}.
 */
@Service
@Transactional
public class ScheduledTransactionService {

    private final String ENTITY_NAME = "Scheduled Transaction";

    private final Logger log = LoggerFactory.getLogger(ScheduledTransactionService.class);

    private final ScheduledTransactionRepository scheduledTransactionRepository;

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final EventRepository eventRepository;

    public ScheduledTransactionService(
        ScheduledTransactionRepository scheduledTransactionRepository,
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        EventRepository eventRepository
    ) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Save a scheduledTransaction.
     *
     * @param scheduledTransaction the entity to save.
     * @return the persisted entity.
     */
    public ScheduledTransaction save(ScheduledTransaction scheduledTransaction) {
        log.debug("Request to save ScheduledTransaction : {}", scheduledTransaction);
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(
                    () ->
                        new BadRequestAlertException(
                            "Current user has no details on its account, could not complete action",
                            ENTITY_NAME,
                            "nonuserfound"
                        )
                )
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        scheduledTransaction.setSourceUser(userDetails.get());
        createEvent(EventType.CREATE);
        return scheduledTransactionRepository.save(scheduledTransaction);
    }

    /**
     * Partially update a scheduledTransaction.
     *
     * @param scheduledTransaction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ScheduledTransaction> partialUpdate(ScheduledTransaction scheduledTransaction) {
        log.debug("Request to partially update ScheduledTransaction : {}", scheduledTransaction);

        return scheduledTransactionRepository
            .findById(scheduledTransaction.getId())
            .map(
                existingScheduledTransaction -> {
                    if (scheduledTransaction.getName() != null) {
                        existingScheduledTransaction.setName(scheduledTransaction.getName());
                    }
                    if (scheduledTransaction.getDescription() != null) {
                        existingScheduledTransaction.setDescription(scheduledTransaction.getDescription());
                    }
                    if (scheduledTransaction.getOriginalAmount() != null) {
                        existingScheduledTransaction.setOriginalAmount(scheduledTransaction.getOriginalAmount());
                    }
                    if (scheduledTransaction.getMovementType() != null) {
                        existingScheduledTransaction.setMovementType(scheduledTransaction.getMovementType());
                    }
                    if (scheduledTransaction.getStartDate() != null) {
                        existingScheduledTransaction.setStartDate(scheduledTransaction.getStartDate());
                    }
                    if (scheduledTransaction.getEndDate() != null) {
                        existingScheduledTransaction.setEndDate(scheduledTransaction.getEndDate());
                    }
                    if (scheduledTransaction.getAddToReports() != null) {
                        existingScheduledTransaction.setAddToReports(scheduledTransaction.getAddToReports());
                    }
                    if (scheduledTransaction.getRecurringType() != null) {
                        existingScheduledTransaction.setRecurringType(scheduledTransaction.getRecurringType());
                    }
                    if (scheduledTransaction.getSeparationCount() != null) {
                        existingScheduledTransaction.setSeparationCount(scheduledTransaction.getSeparationCount());
                    }
                    if (scheduledTransaction.getMaxNumberOfOcurrences() != null) {
                        existingScheduledTransaction.setMaxNumberOfOcurrences(scheduledTransaction.getMaxNumberOfOcurrences());
                    }
                    if (scheduledTransaction.getDayOfWeek() != null) {
                        existingScheduledTransaction.setDayOfWeek(scheduledTransaction.getDayOfWeek());
                    }
                    if (scheduledTransaction.getWeekOfMonth() != null) {
                        existingScheduledTransaction.setWeekOfMonth(scheduledTransaction.getWeekOfMonth());
                    }
                    if (scheduledTransaction.getDayOfMonth() != null) {
                        existingScheduledTransaction.setDayOfMonth(scheduledTransaction.getDayOfMonth());
                    }
                    if (scheduledTransaction.getMonthOfYear() != null) {
                        existingScheduledTransaction.setMonthOfYear(scheduledTransaction.getMonthOfYear());
                    }
                    createEvent(EventType.UPDATE);
                    return existingScheduledTransaction;
                }
            )
            .map(scheduledTransactionRepository::save);
    }

    /**
     * Get all the scheduledTransactions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ScheduledTransaction> findAll() {
        log.debug("Request to get all ScheduledTransactions");
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        List<ScheduledTransaction> entityList = scheduledTransactionRepository.findAll();
        List<ScheduledTransaction> res = new ArrayList<ScheduledTransaction>();
        if (
            (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) &&
            (
                SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER) ||
                SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PREMIUM_USER)
            )
        ) {
            if (userDetails.isPresent()) {
                for (ScheduledTransaction st : entityList) {
                    if (st.getSourceUser() == null) {} else {
                        if (st.getSourceUser().equals(userDetails.get())) {
                            res.add(st);
                            System.out.println(res.size());
                        }
                    }
                }
            }
        } else {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                res = entityList;
            }
        }
        return res;
    }

    /**
     * Get one scheduledTransaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduledTransaction> findOne(Long id) {
        log.debug("Request to get ScheduledTransaction : {}", id);
        return scheduledTransactionRepository.findById(id);
    }

    /**
     * Delete the scheduledTransaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ScheduledTransaction : {}", id);
        createEvent(EventType.DELETE);
        scheduledTransactionRepository.deleteById(id);
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
        event.setSourceEntity(SourceEntity.SCHEDULEDTRANSACTION);
        event.setUserName(user.get().getFirstName());
        event.setUserLastName(user.get().getLastName());
        System.out.println(event);
        eventRepository.save(event);
    }
}
