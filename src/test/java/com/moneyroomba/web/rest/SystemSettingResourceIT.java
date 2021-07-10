package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.SystemSetting;
import com.moneyroomba.repository.SystemSettingRepository;
import com.moneyroomba.service.criteria.SystemSettingCriteria;
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
 * Integration tests for the {@link SystemSettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemSettingResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;
    private static final Double SMALLER_VALUE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/system-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemSettingMockMvc;

    private SystemSetting systemSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemSetting createEntity(EntityManager em) {
        SystemSetting systemSetting = new SystemSetting().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return systemSetting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemSetting createUpdatedEntity(EntityManager em) {
        SystemSetting systemSetting = new SystemSetting().key(UPDATED_KEY).value(UPDATED_VALUE);
        return systemSetting;
    }

    @BeforeEach
    public void initTest() {
        systemSetting = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemSetting() throws Exception {
        int databaseSizeBeforeCreate = systemSettingRepository.findAll().size();
        // Create the SystemSetting
        restSystemSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isCreated());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeCreate + 1);
        SystemSetting testSystemSetting = systemSettingList.get(systemSettingList.size() - 1);
        assertThat(testSystemSetting.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSystemSetting.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createSystemSettingWithExistingId() throws Exception {
        // Create the SystemSetting with an existing ID
        systemSetting.setId(1L);

        int databaseSizeBeforeCreate = systemSettingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemSettingRepository.findAll().size();
        // set the field null
        systemSetting.setKey(null);

        // Create the SystemSetting, which fails.

        restSystemSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemSettingRepository.findAll().size();
        // set the field null
        systemSetting.setValue(null);

        // Create the SystemSetting, which fails.

        restSystemSettingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemSettings() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList
        restSystemSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getSystemSetting() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get the systemSetting
        restSystemSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, systemSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemSetting.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getSystemSettingsByIdFiltering() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        Long id = systemSetting.getId();

        defaultSystemSettingShouldBeFound("id.equals=" + id);
        defaultSystemSettingShouldNotBeFound("id.notEquals=" + id);

        defaultSystemSettingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemSettingShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemSettingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemSettingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where key equals to DEFAULT_KEY
        defaultSystemSettingShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the systemSettingList where key equals to UPDATED_KEY
        defaultSystemSettingShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where key not equals to DEFAULT_KEY
        defaultSystemSettingShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the systemSettingList where key not equals to UPDATED_KEY
        defaultSystemSettingShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where key in DEFAULT_KEY or UPDATED_KEY
        defaultSystemSettingShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the systemSettingList where key equals to UPDATED_KEY
        defaultSystemSettingShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where key is not null
        defaultSystemSettingShouldBeFound("key.specified=true");

        // Get all the systemSettingList where key is null
        defaultSystemSettingShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemSettingsByKeyContainsSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where key contains DEFAULT_KEY
        defaultSystemSettingShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the systemSettingList where key contains UPDATED_KEY
        defaultSystemSettingShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where key does not contain DEFAULT_KEY
        defaultSystemSettingShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the systemSettingList where key does not contain UPDATED_KEY
        defaultSystemSettingShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value equals to DEFAULT_VALUE
        defaultSystemSettingShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the systemSettingList where value equals to UPDATED_VALUE
        defaultSystemSettingShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value not equals to DEFAULT_VALUE
        defaultSystemSettingShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the systemSettingList where value not equals to UPDATED_VALUE
        defaultSystemSettingShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSystemSettingShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the systemSettingList where value equals to UPDATED_VALUE
        defaultSystemSettingShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value is not null
        defaultSystemSettingShouldBeFound("value.specified=true");

        // Get all the systemSettingList where value is null
        defaultSystemSettingShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value is greater than or equal to DEFAULT_VALUE
        defaultSystemSettingShouldBeFound("value.greaterThanOrEqual=" + DEFAULT_VALUE);

        // Get all the systemSettingList where value is greater than or equal to UPDATED_VALUE
        defaultSystemSettingShouldNotBeFound("value.greaterThanOrEqual=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value is less than or equal to DEFAULT_VALUE
        defaultSystemSettingShouldBeFound("value.lessThanOrEqual=" + DEFAULT_VALUE);

        // Get all the systemSettingList where value is less than or equal to SMALLER_VALUE
        defaultSystemSettingShouldNotBeFound("value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value is less than DEFAULT_VALUE
        defaultSystemSettingShouldNotBeFound("value.lessThan=" + DEFAULT_VALUE);

        // Get all the systemSettingList where value is less than UPDATED_VALUE
        defaultSystemSettingShouldBeFound("value.lessThan=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllSystemSettingsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        // Get all the systemSettingList where value is greater than DEFAULT_VALUE
        defaultSystemSettingShouldNotBeFound("value.greaterThan=" + DEFAULT_VALUE);

        // Get all the systemSettingList where value is greater than SMALLER_VALUE
        defaultSystemSettingShouldBeFound("value.greaterThan=" + SMALLER_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemSettingShouldBeFound(String filter) throws Exception {
        restSystemSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));

        // Check, that the count call also returns 1
        restSystemSettingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemSettingShouldNotBeFound(String filter) throws Exception {
        restSystemSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemSettingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemSetting() throws Exception {
        // Get the systemSetting
        restSystemSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemSetting() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();

        // Update the systemSetting
        SystemSetting updatedSystemSetting = systemSettingRepository.findById(systemSetting.getId()).get();
        // Disconnect from session so that the updates on updatedSystemSetting are not directly saved in db
        em.detach(updatedSystemSetting);
        updatedSystemSetting.key(UPDATED_KEY).value(UPDATED_VALUE);

        restSystemSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemSetting.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSystemSetting))
            )
            .andExpect(status().isOk());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
        SystemSetting testSystemSetting = systemSettingList.get(systemSettingList.size() - 1);
        assertThat(testSystemSetting.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSystemSetting.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingSystemSetting() throws Exception {
        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();
        systemSetting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemSetting.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemSetting() throws Exception {
        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();
        systemSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemSetting() throws Exception {
        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();
        systemSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemSettingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemSettingWithPatch() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();

        // Update the systemSetting using partial update
        SystemSetting partialUpdatedSystemSetting = new SystemSetting();
        partialUpdatedSystemSetting.setId(systemSetting.getId());

        partialUpdatedSystemSetting.key(UPDATED_KEY).value(UPDATED_VALUE);

        restSystemSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemSetting.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemSetting))
            )
            .andExpect(status().isOk());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
        SystemSetting testSystemSetting = systemSettingList.get(systemSettingList.size() - 1);
        assertThat(testSystemSetting.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSystemSetting.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateSystemSettingWithPatch() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();

        // Update the systemSetting using partial update
        SystemSetting partialUpdatedSystemSetting = new SystemSetting();
        partialUpdatedSystemSetting.setId(systemSetting.getId());

        partialUpdatedSystemSetting.key(UPDATED_KEY).value(UPDATED_VALUE);

        restSystemSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemSetting.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemSetting))
            )
            .andExpect(status().isOk());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
        SystemSetting testSystemSetting = systemSettingList.get(systemSettingList.size() - 1);
        assertThat(testSystemSetting.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSystemSetting.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemSetting() throws Exception {
        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();
        systemSetting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemSetting.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemSetting() throws Exception {
        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();
        systemSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemSetting() throws Exception {
        int databaseSizeBeforeUpdate = systemSettingRepository.findAll().size();
        systemSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemSettingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemSetting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemSetting in the database
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemSetting() throws Exception {
        // Initialize the database
        systemSettingRepository.saveAndFlush(systemSetting);

        int databaseSizeBeforeDelete = systemSettingRepository.findAll().size();

        // Delete the systemSetting
        restSystemSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemSetting.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemSetting> systemSettingList = systemSettingRepository.findAll();
        assertThat(systemSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
