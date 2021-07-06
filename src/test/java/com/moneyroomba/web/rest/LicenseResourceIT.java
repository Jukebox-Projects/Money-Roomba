package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Invoice;
import com.moneyroomba.domain.License;
import com.moneyroomba.domain.enumeration.LicenseCreateMethod;
import com.moneyroomba.repository.LicenseRepository;
import com.moneyroomba.service.criteria.LicenseCriteria;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link LicenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LicenseResourceIT {

    private static final UUID DEFAULT_CODE = UUID.randomUUID();
    private static final UUID UPDATED_CODE = UUID.randomUUID();

    private static final Boolean DEFAULT_IS_ASSIGNED = false;
    private static final Boolean UPDATED_IS_ASSIGNED = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LicenseCreateMethod DEFAULT_CREATE_METHOD = LicenseCreateMethod.MANUAL;
    private static final LicenseCreateMethod UPDATED_CREATE_METHOD = LicenseCreateMethod.BULK;

    private static final String ENTITY_API_URL = "/api/licenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLicenseMockMvc;

    private License license;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createEntity(EntityManager em) {
        License license = new License()
            .code(DEFAULT_CODE)
            .isAssigned(DEFAULT_IS_ASSIGNED)
            .isActive(DEFAULT_IS_ACTIVE)
            .createMethod(DEFAULT_CREATE_METHOD);
        return license;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createUpdatedEntity(EntityManager em) {
        License license = new License()
            .code(UPDATED_CODE)
            .isAssigned(UPDATED_IS_ASSIGNED)
            .isActive(UPDATED_IS_ACTIVE)
            .createMethod(UPDATED_CREATE_METHOD);
        return license;
    }

    @BeforeEach
    public void initTest() {
        license = createEntity(em);
    }

    @Test
    @Transactional
    void createLicense() throws Exception {
        int databaseSizeBeforeCreate = licenseRepository.findAll().size();
        // Create the License
        restLicenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isCreated());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeCreate + 1);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLicense.getIsAssigned()).isEqualTo(DEFAULT_IS_ASSIGNED);
        assertThat(testLicense.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testLicense.getCreateMethod()).isEqualTo(DEFAULT_CREATE_METHOD);
    }

    @Test
    @Transactional
    void createLicenseWithExistingId() throws Exception {
        // Create the License with an existing ID
        license.setId(1L);

        int databaseSizeBeforeCreate = licenseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setCode(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAssignedIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setIsAssigned(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setIsActive(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setCreateMethod(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLicenses() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(license.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].isAssigned").value(hasItem(DEFAULT_IS_ASSIGNED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createMethod").value(hasItem(DEFAULT_CREATE_METHOD.toString())));
    }

    @Test
    @Transactional
    void getLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get the license
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL_ID, license.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(license.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.isAssigned").value(DEFAULT_IS_ASSIGNED.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createMethod").value(DEFAULT_CREATE_METHOD.toString()));
    }

    @Test
    @Transactional
    void getLicensesByIdFiltering() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        Long id = license.getId();

        defaultLicenseShouldBeFound("id.equals=" + id);
        defaultLicenseShouldNotBeFound("id.notEquals=" + id);

        defaultLicenseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLicenseShouldNotBeFound("id.greaterThan=" + id);

        defaultLicenseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLicenseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLicensesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where code equals to DEFAULT_CODE
        defaultLicenseShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the licenseList where code equals to UPDATED_CODE
        defaultLicenseShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLicensesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where code not equals to DEFAULT_CODE
        defaultLicenseShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the licenseList where code not equals to UPDATED_CODE
        defaultLicenseShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLicensesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where code in DEFAULT_CODE or UPDATED_CODE
        defaultLicenseShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the licenseList where code equals to UPDATED_CODE
        defaultLicenseShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLicensesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where code is not null
        defaultLicenseShouldBeFound("code.specified=true");

        // Get all the licenseList where code is null
        defaultLicenseShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllLicensesByIsAssignedIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isAssigned equals to DEFAULT_IS_ASSIGNED
        defaultLicenseShouldBeFound("isAssigned.equals=" + DEFAULT_IS_ASSIGNED);

        // Get all the licenseList where isAssigned equals to UPDATED_IS_ASSIGNED
        defaultLicenseShouldNotBeFound("isAssigned.equals=" + UPDATED_IS_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllLicensesByIsAssignedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isAssigned not equals to DEFAULT_IS_ASSIGNED
        defaultLicenseShouldNotBeFound("isAssigned.notEquals=" + DEFAULT_IS_ASSIGNED);

        // Get all the licenseList where isAssigned not equals to UPDATED_IS_ASSIGNED
        defaultLicenseShouldBeFound("isAssigned.notEquals=" + UPDATED_IS_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllLicensesByIsAssignedIsInShouldWork() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isAssigned in DEFAULT_IS_ASSIGNED or UPDATED_IS_ASSIGNED
        defaultLicenseShouldBeFound("isAssigned.in=" + DEFAULT_IS_ASSIGNED + "," + UPDATED_IS_ASSIGNED);

        // Get all the licenseList where isAssigned equals to UPDATED_IS_ASSIGNED
        defaultLicenseShouldNotBeFound("isAssigned.in=" + UPDATED_IS_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllLicensesByIsAssignedIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isAssigned is not null
        defaultLicenseShouldBeFound("isAssigned.specified=true");

        // Get all the licenseList where isAssigned is null
        defaultLicenseShouldNotBeFound("isAssigned.specified=false");
    }

    @Test
    @Transactional
    void getAllLicensesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isActive equals to DEFAULT_IS_ACTIVE
        defaultLicenseShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the licenseList where isActive equals to UPDATED_IS_ACTIVE
        defaultLicenseShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLicensesByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultLicenseShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the licenseList where isActive not equals to UPDATED_IS_ACTIVE
        defaultLicenseShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLicensesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultLicenseShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the licenseList where isActive equals to UPDATED_IS_ACTIVE
        defaultLicenseShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLicensesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where isActive is not null
        defaultLicenseShouldBeFound("isActive.specified=true");

        // Get all the licenseList where isActive is null
        defaultLicenseShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllLicensesByCreateMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where createMethod equals to DEFAULT_CREATE_METHOD
        defaultLicenseShouldBeFound("createMethod.equals=" + DEFAULT_CREATE_METHOD);

        // Get all the licenseList where createMethod equals to UPDATED_CREATE_METHOD
        defaultLicenseShouldNotBeFound("createMethod.equals=" + UPDATED_CREATE_METHOD);
    }

    @Test
    @Transactional
    void getAllLicensesByCreateMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where createMethod not equals to DEFAULT_CREATE_METHOD
        defaultLicenseShouldNotBeFound("createMethod.notEquals=" + DEFAULT_CREATE_METHOD);

        // Get all the licenseList where createMethod not equals to UPDATED_CREATE_METHOD
        defaultLicenseShouldBeFound("createMethod.notEquals=" + UPDATED_CREATE_METHOD);
    }

    @Test
    @Transactional
    void getAllLicensesByCreateMethodIsInShouldWork() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where createMethod in DEFAULT_CREATE_METHOD or UPDATED_CREATE_METHOD
        defaultLicenseShouldBeFound("createMethod.in=" + DEFAULT_CREATE_METHOD + "," + UPDATED_CREATE_METHOD);

        // Get all the licenseList where createMethod equals to UPDATED_CREATE_METHOD
        defaultLicenseShouldNotBeFound("createMethod.in=" + UPDATED_CREATE_METHOD);
    }

    @Test
    @Transactional
    void getAllLicensesByCreateMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList where createMethod is not null
        defaultLicenseShouldBeFound("createMethod.specified=true");

        // Get all the licenseList where createMethod is null
        defaultLicenseShouldNotBeFound("createMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllLicensesByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);
        Invoice invoice = InvoiceResourceIT.createEntity(em);
        em.persist(invoice);
        em.flush();
        license.setInvoice(invoice);
        licenseRepository.saveAndFlush(license);
        Long invoiceId = invoice.getId();

        // Get all the licenseList where invoice equals to invoiceId
        defaultLicenseShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the licenseList where invoice equals to (invoiceId + 1)
        defaultLicenseShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLicenseShouldBeFound(String filter) throws Exception {
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(license.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].isAssigned").value(hasItem(DEFAULT_IS_ASSIGNED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createMethod").value(hasItem(DEFAULT_CREATE_METHOD.toString())));

        // Check, that the count call also returns 1
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLicenseShouldNotBeFound(String filter) throws Exception {
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLicense() throws Exception {
        // Get the license
        restLicenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();

        // Update the license
        License updatedLicense = licenseRepository.findById(license.getId()).get();
        // Disconnect from session so that the updates on updatedLicense are not directly saved in db
        em.detach(updatedLicense);
        updatedLicense.code(UPDATED_CODE).isAssigned(UPDATED_IS_ASSIGNED).isActive(UPDATED_IS_ACTIVE).createMethod(UPDATED_CREATE_METHOD);

        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLicense.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLicense))
            )
            .andExpect(status().isOk());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLicense.getIsAssigned()).isEqualTo(UPDATED_IS_ASSIGNED);
        assertThat(testLicense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLicense.getCreateMethod()).isEqualTo(UPDATED_CREATE_METHOD);
    }

    @Test
    @Transactional
    void putNonExistingLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();
        license.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, license.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();
        license.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();
        license.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLicenseWithPatch() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();

        // Update the license using partial update
        License partialUpdatedLicense = new License();
        partialUpdatedLicense.setId(license.getId());

        partialUpdatedLicense.code(UPDATED_CODE).isActive(UPDATED_IS_ACTIVE);

        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLicense.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLicense))
            )
            .andExpect(status().isOk());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLicense.getIsAssigned()).isEqualTo(DEFAULT_IS_ASSIGNED);
        assertThat(testLicense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLicense.getCreateMethod()).isEqualTo(DEFAULT_CREATE_METHOD);
    }

    @Test
    @Transactional
    void fullUpdateLicenseWithPatch() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();

        // Update the license using partial update
        License partialUpdatedLicense = new License();
        partialUpdatedLicense.setId(license.getId());

        partialUpdatedLicense
            .code(UPDATED_CODE)
            .isAssigned(UPDATED_IS_ASSIGNED)
            .isActive(UPDATED_IS_ACTIVE)
            .createMethod(UPDATED_CREATE_METHOD);

        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLicense.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLicense))
            )
            .andExpect(status().isOk());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLicense.getIsAssigned()).isEqualTo(UPDATED_IS_ASSIGNED);
        assertThat(testLicense.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testLicense.getCreateMethod()).isEqualTo(UPDATED_CREATE_METHOD);
    }

    @Test
    @Transactional
    void patchNonExistingLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();
        license.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, license.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();
        license.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();
        license.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(license))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        int databaseSizeBeforeDelete = licenseRepository.findAll().size();

        // Delete the license
        restLicenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, license.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
