package com.moneyroomba.web.rest;

import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.repository.ScheduledTransactionRepository;
import com.moneyroomba.service.ScheduledTransactionQueryService;
import com.moneyroomba.service.ScheduledTransactionService;
import com.moneyroomba.service.criteria.ScheduledTransactionCriteria;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moneyroomba.domain.ScheduledTransaction}.
 */
@RestController
@RequestMapping("/api")
public class ScheduledTransactionResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledTransactionResource.class);

    private static final String ENTITY_NAME = "scheduledTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduledTransactionService scheduledTransactionService;

    private final ScheduledTransactionRepository scheduledTransactionRepository;

    private final ScheduledTransactionQueryService scheduledTransactionQueryService;

    public ScheduledTransactionResource(
        ScheduledTransactionService scheduledTransactionService,
        ScheduledTransactionRepository scheduledTransactionRepository,
        ScheduledTransactionQueryService scheduledTransactionQueryService
    ) {
        this.scheduledTransactionService = scheduledTransactionService;
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.scheduledTransactionQueryService = scheduledTransactionQueryService;
    }

    /**
     * {@code POST  /scheduled-transactions} : Create a new scheduledTransaction.
     *
     * @param scheduledTransaction the scheduledTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduledTransaction, or with status {@code 400 (Bad Request)} if the scheduledTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scheduled-transactions")
    public ResponseEntity<ScheduledTransaction> createScheduledTransaction(@Valid @RequestBody ScheduledTransaction scheduledTransaction)
        throws URISyntaxException {
        log.debug("REST request to save ScheduledTransaction : {}", scheduledTransaction);
        if (scheduledTransaction.getId() != null) {
            throw new BadRequestAlertException("A new scheduledTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduledTransaction result = scheduledTransactionService.save(scheduledTransaction);
        return ResponseEntity
            .created(new URI("/api/scheduled-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scheduled-transactions/:id} : Updates an existing scheduledTransaction.
     *
     * @param id the id of the scheduledTransaction to save.
     * @param scheduledTransaction the scheduledTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledTransaction,
     * or with status {@code 400 (Bad Request)} if the scheduledTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduledTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scheduled-transactions/{id}")
    public ResponseEntity<ScheduledTransaction> updateScheduledTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScheduledTransaction scheduledTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduledTransaction : {}, {}", id, scheduledTransaction);
        if (scheduledTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScheduledTransaction result = scheduledTransactionService.save(scheduledTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduledTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scheduled-transactions/:id} : Partial updates given fields of an existing scheduledTransaction, field will ignore if it is null
     *
     * @param id the id of the scheduledTransaction to save.
     * @param scheduledTransaction the scheduledTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledTransaction,
     * or with status {@code 400 (Bad Request)} if the scheduledTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the scheduledTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduledTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scheduled-transactions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ScheduledTransaction> partialUpdateScheduledTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScheduledTransaction scheduledTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduledTransaction partially : {}, {}", id, scheduledTransaction);
        if (scheduledTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduledTransaction> result = scheduledTransactionService.partialUpdate(scheduledTransaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduledTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduled-transactions} : get all the scheduledTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduledTransactions in body.
     */
    @GetMapping("/scheduled-transactions")
    public ResponseEntity<List<ScheduledTransaction>> getAllScheduledTransactions(ScheduledTransactionCriteria criteria) {
        log.debug("REST request to get ScheduledTransactions by criteria: {}", criteria);
        List<ScheduledTransaction> entityList = scheduledTransactionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /scheduled-transactions/count} : count all the scheduledTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/scheduled-transactions/count")
    public ResponseEntity<Long> countScheduledTransactions(ScheduledTransactionCriteria criteria) {
        log.debug("REST request to count ScheduledTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(scheduledTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scheduled-transactions/:id} : get the "id" scheduledTransaction.
     *
     * @param id the id of the scheduledTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduledTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scheduled-transactions/{id}")
    public ResponseEntity<ScheduledTransaction> getScheduledTransaction(@PathVariable Long id) {
        log.debug("REST request to get ScheduledTransaction : {}", id);
        Optional<ScheduledTransaction> scheduledTransaction = scheduledTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledTransaction);
    }

    /**
     * {@code DELETE  /scheduled-transactions/:id} : delete the "id" scheduledTransaction.
     *
     * @param id the id of the scheduledTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scheduled-transactions/{id}")
    public ResponseEntity<Void> deleteScheduledTransaction(@PathVariable Long id) {
        log.debug("REST request to delete ScheduledTransaction : {}", id);
        scheduledTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
