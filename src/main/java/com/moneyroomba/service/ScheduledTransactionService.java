package com.moneyroomba.service;

import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.repository.ScheduledTransactionRepository;
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

    private final Logger log = LoggerFactory.getLogger(ScheduledTransactionService.class);

    private final ScheduledTransactionRepository scheduledTransactionRepository;

    public ScheduledTransactionService(ScheduledTransactionRepository scheduledTransactionRepository) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
    }

    /**
     * Save a scheduledTransaction.
     *
     * @param scheduledTransaction the entity to save.
     * @return the persisted entity.
     */
    public ScheduledTransaction save(ScheduledTransaction scheduledTransaction) {
        log.debug("Request to save ScheduledTransaction : {}", scheduledTransaction);
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
        return scheduledTransactionRepository.findAll();
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
        scheduledTransactionRepository.deleteById(id);
    }
}
