package com.moneyroomba.web.rest;

import com.moneyroomba.domain.SchedulePattern;
import com.moneyroomba.repository.SchedulePatternRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moneyroomba.domain.SchedulePattern}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SchedulePatternResource {

    private final Logger log = LoggerFactory.getLogger(SchedulePatternResource.class);

    private static final String ENTITY_NAME = "schedulePattern";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedulePatternRepository schedulePatternRepository;

    public SchedulePatternResource(SchedulePatternRepository schedulePatternRepository) {
        this.schedulePatternRepository = schedulePatternRepository;
    }

    /**
     * {@code POST  /schedule-patterns} : Create a new schedulePattern.
     *
     * @param schedulePattern the schedulePattern to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedulePattern, or with status {@code 400 (Bad Request)} if the schedulePattern has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedule-patterns")
    public ResponseEntity<SchedulePattern> createSchedulePattern(@Valid @RequestBody SchedulePattern schedulePattern)
        throws URISyntaxException {
        log.debug("REST request to save SchedulePattern : {}", schedulePattern);
        if (schedulePattern.getId() != null) {
            throw new BadRequestAlertException("A new schedulePattern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SchedulePattern result = schedulePatternRepository.save(schedulePattern);
        return ResponseEntity
            .created(new URI("/api/schedule-patterns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedule-patterns/:id} : Updates an existing schedulePattern.
     *
     * @param id the id of the schedulePattern to save.
     * @param schedulePattern the schedulePattern to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulePattern,
     * or with status {@code 400 (Bad Request)} if the schedulePattern is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedulePattern couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedule-patterns/{id}")
    public ResponseEntity<SchedulePattern> updateSchedulePattern(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SchedulePattern schedulePattern
    ) throws URISyntaxException {
        log.debug("REST request to update SchedulePattern : {}, {}", id, schedulePattern);
        if (schedulePattern.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulePattern.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulePatternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SchedulePattern result = schedulePatternRepository.save(schedulePattern);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulePattern.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /schedule-patterns/:id} : Partial updates given fields of an existing schedulePattern, field will ignore if it is null
     *
     * @param id the id of the schedulePattern to save.
     * @param schedulePattern the schedulePattern to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedulePattern,
     * or with status {@code 400 (Bad Request)} if the schedulePattern is not valid,
     * or with status {@code 404 (Not Found)} if the schedulePattern is not found,
     * or with status {@code 500 (Internal Server Error)} if the schedulePattern couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/schedule-patterns/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SchedulePattern> partialUpdateSchedulePattern(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SchedulePattern schedulePattern
    ) throws URISyntaxException {
        log.debug("REST request to partial update SchedulePattern partially : {}, {}", id, schedulePattern);
        if (schedulePattern.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schedulePattern.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schedulePatternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchedulePattern> result = schedulePatternRepository
            .findById(schedulePattern.getId())
            .map(
                existingSchedulePattern -> {
                    if (schedulePattern.getRecurringType() != null) {
                        existingSchedulePattern.setRecurringType(schedulePattern.getRecurringType());
                    }
                    if (schedulePattern.getSeparationCount() != null) {
                        existingSchedulePattern.setSeparationCount(schedulePattern.getSeparationCount());
                    }
                    if (schedulePattern.getMaxNumberOfOcurrences() != null) {
                        existingSchedulePattern.setMaxNumberOfOcurrences(schedulePattern.getMaxNumberOfOcurrences());
                    }
                    if (schedulePattern.getDayOfWeek() != null) {
                        existingSchedulePattern.setDayOfWeek(schedulePattern.getDayOfWeek());
                    }
                    if (schedulePattern.getWeekOfMonth() != null) {
                        existingSchedulePattern.setWeekOfMonth(schedulePattern.getWeekOfMonth());
                    }
                    if (schedulePattern.getDayOfMonth() != null) {
                        existingSchedulePattern.setDayOfMonth(schedulePattern.getDayOfMonth());
                    }
                    if (schedulePattern.getMonthOfYear() != null) {
                        existingSchedulePattern.setMonthOfYear(schedulePattern.getMonthOfYear());
                    }

                    return existingSchedulePattern;
                }
            )
            .map(schedulePatternRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schedulePattern.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-patterns} : get all the schedulePatterns.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulePatterns in body.
     */
    @GetMapping("/schedule-patterns")
    public List<SchedulePattern> getAllSchedulePatterns() {
        log.debug("REST request to get all SchedulePatterns");
        return schedulePatternRepository.findAll();
    }

    /**
     * {@code GET  /schedule-patterns/:id} : get the "id" schedulePattern.
     *
     * @param id the id of the schedulePattern to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedulePattern, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedule-patterns/{id}")
    public ResponseEntity<SchedulePattern> getSchedulePattern(@PathVariable Long id) {
        log.debug("REST request to get SchedulePattern : {}", id);
        Optional<SchedulePattern> schedulePattern = schedulePatternRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(schedulePattern);
    }

    /**
     * {@code DELETE  /schedule-patterns/:id} : delete the "id" schedulePattern.
     *
     * @param id the id of the schedulePattern to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedule-patterns/{id}")
    public ResponseEntity<Void> deleteSchedulePattern(@PathVariable Long id) {
        log.debug("REST request to delete SchedulePattern : {}", id);
        schedulePatternRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
