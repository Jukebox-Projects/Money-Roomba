package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.SchedulePattern;
import com.moneyroomba.domain.enumeration.RecurringType;
import com.moneyroomba.repository.SchedulePatternRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SchedulePatternResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SchedulePatternResourceIT {

    private static final RecurringType DEFAULT_RECURRING_TYPE = RecurringType.DAILY;
    private static final RecurringType UPDATED_RECURRING_TYPE = RecurringType.WEEKLY;

    private static final Integer DEFAULT_SEPARATION_COUNT = 0;
    private static final Integer UPDATED_SEPARATION_COUNT = 1;

    private static final Integer DEFAULT_MAX_NUMBER_OF_OCURRENCES = 1;
    private static final Integer UPDATED_MAX_NUMBER_OF_OCURRENCES = 2;

    private static final Integer DEFAULT_DAY_OF_WEEK = 0;
    private static final Integer UPDATED_DAY_OF_WEEK = 1;

    private static final Integer DEFAULT_WEEK_OF_MONTH = 0;
    private static final Integer UPDATED_WEEK_OF_MONTH = 1;

    private static final Integer DEFAULT_DAY_OF_MONTH = 0;
    private static final Integer UPDATED_DAY_OF_MONTH = 1;

    private static final Integer DEFAULT_MONTH_OF_YEAR = 0;
    private static final Integer UPDATED_MONTH_OF_YEAR = 1;

    private static final String ENTITY_API_URL = "/api/schedule-patterns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SchedulePatternRepository schedulePatternRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSchedulePatternMockMvc;

    private SchedulePattern schedulePattern;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulePattern createEntity(EntityManager em) {
        SchedulePattern schedulePattern = new SchedulePattern()
            .recurringType(DEFAULT_RECURRING_TYPE)
            .separationCount(DEFAULT_SEPARATION_COUNT)
            .maxNumberOfOcurrences(DEFAULT_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .weekOfMonth(DEFAULT_WEEK_OF_MONTH)
            .dayOfMonth(DEFAULT_DAY_OF_MONTH)
            .monthOfYear(DEFAULT_MONTH_OF_YEAR);
        return schedulePattern;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SchedulePattern createUpdatedEntity(EntityManager em) {
        SchedulePattern schedulePattern = new SchedulePattern()
            .recurringType(UPDATED_RECURRING_TYPE)
            .separationCount(UPDATED_SEPARATION_COUNT)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH)
            .monthOfYear(UPDATED_MONTH_OF_YEAR);
        return schedulePattern;
    }

    @BeforeEach
    public void initTest() {
        schedulePattern = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedulePattern() throws Exception {
        int databaseSizeBeforeCreate = schedulePatternRepository.findAll().size();
        // Create the SchedulePattern
        restSchedulePatternMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isCreated());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeCreate + 1);
        SchedulePattern testSchedulePattern = schedulePatternList.get(schedulePatternList.size() - 1);
        assertThat(testSchedulePattern.getRecurringType()).isEqualTo(DEFAULT_RECURRING_TYPE);
        assertThat(testSchedulePattern.getSeparationCount()).isEqualTo(DEFAULT_SEPARATION_COUNT);
        assertThat(testSchedulePattern.getMaxNumberOfOcurrences()).isEqualTo(DEFAULT_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testSchedulePattern.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testSchedulePattern.getWeekOfMonth()).isEqualTo(DEFAULT_WEEK_OF_MONTH);
        assertThat(testSchedulePattern.getDayOfMonth()).isEqualTo(DEFAULT_DAY_OF_MONTH);
        assertThat(testSchedulePattern.getMonthOfYear()).isEqualTo(DEFAULT_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void createSchedulePatternWithExistingId() throws Exception {
        // Create the SchedulePattern with an existing ID
        schedulePattern.setId(1L);

        int databaseSizeBeforeCreate = schedulePatternRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulePatternMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecurringTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = schedulePatternRepository.findAll().size();
        // set the field null
        schedulePattern.setRecurringType(null);

        // Create the SchedulePattern, which fails.

        restSchedulePatternMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isBadRequest());

        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchedulePatterns() throws Exception {
        // Initialize the database
        schedulePatternRepository.saveAndFlush(schedulePattern);

        // Get all the schedulePatternList
        restSchedulePatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedulePattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].recurringType").value(hasItem(DEFAULT_RECURRING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].separationCount").value(hasItem(DEFAULT_SEPARATION_COUNT)))
            .andExpect(jsonPath("$.[*].maxNumberOfOcurrences").value(hasItem(DEFAULT_MAX_NUMBER_OF_OCURRENCES)))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].weekOfMonth").value(hasItem(DEFAULT_WEEK_OF_MONTH)))
            .andExpect(jsonPath("$.[*].dayOfMonth").value(hasItem(DEFAULT_DAY_OF_MONTH)))
            .andExpect(jsonPath("$.[*].monthOfYear").value(hasItem(DEFAULT_MONTH_OF_YEAR)));
    }

    @Test
    @Transactional
    void getSchedulePattern() throws Exception {
        // Initialize the database
        schedulePatternRepository.saveAndFlush(schedulePattern);

        // Get the schedulePattern
        restSchedulePatternMockMvc
            .perform(get(ENTITY_API_URL_ID, schedulePattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedulePattern.getId().intValue()))
            .andExpect(jsonPath("$.recurringType").value(DEFAULT_RECURRING_TYPE.toString()))
            .andExpect(jsonPath("$.separationCount").value(DEFAULT_SEPARATION_COUNT))
            .andExpect(jsonPath("$.maxNumberOfOcurrences").value(DEFAULT_MAX_NUMBER_OF_OCURRENCES))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK))
            .andExpect(jsonPath("$.weekOfMonth").value(DEFAULT_WEEK_OF_MONTH))
            .andExpect(jsonPath("$.dayOfMonth").value(DEFAULT_DAY_OF_MONTH))
            .andExpect(jsonPath("$.monthOfYear").value(DEFAULT_MONTH_OF_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingSchedulePattern() throws Exception {
        // Get the schedulePattern
        restSchedulePatternMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSchedulePattern() throws Exception {
        // Initialize the database
        schedulePatternRepository.saveAndFlush(schedulePattern);

        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();

        // Update the schedulePattern
        SchedulePattern updatedSchedulePattern = schedulePatternRepository.findById(schedulePattern.getId()).get();
        // Disconnect from session so that the updates on updatedSchedulePattern are not directly saved in db
        em.detach(updatedSchedulePattern);
        updatedSchedulePattern
            .recurringType(UPDATED_RECURRING_TYPE)
            .separationCount(UPDATED_SEPARATION_COUNT)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH)
            .monthOfYear(UPDATED_MONTH_OF_YEAR);

        restSchedulePatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSchedulePattern.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSchedulePattern))
            )
            .andExpect(status().isOk());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
        SchedulePattern testSchedulePattern = schedulePatternList.get(schedulePatternList.size() - 1);
        assertThat(testSchedulePattern.getRecurringType()).isEqualTo(UPDATED_RECURRING_TYPE);
        assertThat(testSchedulePattern.getSeparationCount()).isEqualTo(UPDATED_SEPARATION_COUNT);
        assertThat(testSchedulePattern.getMaxNumberOfOcurrences()).isEqualTo(UPDATED_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testSchedulePattern.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testSchedulePattern.getWeekOfMonth()).isEqualTo(UPDATED_WEEK_OF_MONTH);
        assertThat(testSchedulePattern.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testSchedulePattern.getMonthOfYear()).isEqualTo(UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingSchedulePattern() throws Exception {
        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();
        schedulePattern.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulePatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, schedulePattern.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedulePattern() throws Exception {
        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();
        schedulePattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulePatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedulePattern() throws Exception {
        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();
        schedulePattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulePatternMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSchedulePatternWithPatch() throws Exception {
        // Initialize the database
        schedulePatternRepository.saveAndFlush(schedulePattern);

        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();

        // Update the schedulePattern using partial update
        SchedulePattern partialUpdatedSchedulePattern = new SchedulePattern();
        partialUpdatedSchedulePattern.setId(schedulePattern.getId());

        partialUpdatedSchedulePattern
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH);

        restSchedulePatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulePattern.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulePattern))
            )
            .andExpect(status().isOk());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
        SchedulePattern testSchedulePattern = schedulePatternList.get(schedulePatternList.size() - 1);
        assertThat(testSchedulePattern.getRecurringType()).isEqualTo(DEFAULT_RECURRING_TYPE);
        assertThat(testSchedulePattern.getSeparationCount()).isEqualTo(DEFAULT_SEPARATION_COUNT);
        assertThat(testSchedulePattern.getMaxNumberOfOcurrences()).isEqualTo(UPDATED_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testSchedulePattern.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testSchedulePattern.getWeekOfMonth()).isEqualTo(UPDATED_WEEK_OF_MONTH);
        assertThat(testSchedulePattern.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testSchedulePattern.getMonthOfYear()).isEqualTo(DEFAULT_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateSchedulePatternWithPatch() throws Exception {
        // Initialize the database
        schedulePatternRepository.saveAndFlush(schedulePattern);

        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();

        // Update the schedulePattern using partial update
        SchedulePattern partialUpdatedSchedulePattern = new SchedulePattern();
        partialUpdatedSchedulePattern.setId(schedulePattern.getId());

        partialUpdatedSchedulePattern
            .recurringType(UPDATED_RECURRING_TYPE)
            .separationCount(UPDATED_SEPARATION_COUNT)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH)
            .monthOfYear(UPDATED_MONTH_OF_YEAR);

        restSchedulePatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedulePattern.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedulePattern))
            )
            .andExpect(status().isOk());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
        SchedulePattern testSchedulePattern = schedulePatternList.get(schedulePatternList.size() - 1);
        assertThat(testSchedulePattern.getRecurringType()).isEqualTo(UPDATED_RECURRING_TYPE);
        assertThat(testSchedulePattern.getSeparationCount()).isEqualTo(UPDATED_SEPARATION_COUNT);
        assertThat(testSchedulePattern.getMaxNumberOfOcurrences()).isEqualTo(UPDATED_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testSchedulePattern.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testSchedulePattern.getWeekOfMonth()).isEqualTo(UPDATED_WEEK_OF_MONTH);
        assertThat(testSchedulePattern.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testSchedulePattern.getMonthOfYear()).isEqualTo(UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingSchedulePattern() throws Exception {
        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();
        schedulePattern.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedulePatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, schedulePattern.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedulePattern() throws Exception {
        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();
        schedulePattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulePatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isBadRequest());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedulePattern() throws Exception {
        int databaseSizeBeforeUpdate = schedulePatternRepository.findAll().size();
        schedulePattern.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSchedulePatternMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(schedulePattern))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SchedulePattern in the database
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchedulePattern() throws Exception {
        // Initialize the database
        schedulePatternRepository.saveAndFlush(schedulePattern);

        int databaseSizeBeforeDelete = schedulePatternRepository.findAll().size();

        // Delete the schedulePattern
        restSchedulePatternMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedulePattern.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SchedulePattern> schedulePatternList = schedulePatternRepository.findAll();
        assertThat(schedulePatternList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
