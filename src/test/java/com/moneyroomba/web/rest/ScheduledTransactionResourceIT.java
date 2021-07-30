package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.RecurringType;
import com.moneyroomba.repository.ScheduledTransactionRepository;
import com.moneyroomba.service.criteria.ScheduledTransactionCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ScheduledTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduledTransactionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_ORIGINAL_AMOUNT = 0D;
    private static final Double UPDATED_ORIGINAL_AMOUNT = 1D;
    private static final Double SMALLER_ORIGINAL_AMOUNT = 0D - 1D;

    private static final MovementType DEFAULT_MOVEMENT_TYPE = MovementType.INCOME;
    private static final MovementType UPDATED_MOVEMENT_TYPE = MovementType.EXPENSE;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ADD_TO_REPORTS = false;
    private static final Boolean UPDATED_ADD_TO_REPORTS = true;

    private static final RecurringType DEFAULT_RECURRING_TYPE = RecurringType.DAILY;
    private static final RecurringType UPDATED_RECURRING_TYPE = RecurringType.WEEKLY;

    private static final Integer DEFAULT_SEPARATION_COUNT = 0;
    private static final Integer UPDATED_SEPARATION_COUNT = 1;
    private static final Integer SMALLER_SEPARATION_COUNT = 0 - 1;

    private static final Integer DEFAULT_MAX_NUMBER_OF_OCURRENCES = 1;
    private static final Integer UPDATED_MAX_NUMBER_OF_OCURRENCES = 2;
    private static final Integer SMALLER_MAX_NUMBER_OF_OCURRENCES = 1 - 1;

    private static final Integer DEFAULT_DAY_OF_WEEK = 0;
    private static final Integer UPDATED_DAY_OF_WEEK = 1;
    private static final Integer SMALLER_DAY_OF_WEEK = 0 - 1;

    private static final Integer DEFAULT_WEEK_OF_MONTH = 0;
    private static final Integer UPDATED_WEEK_OF_MONTH = 1;
    private static final Integer SMALLER_WEEK_OF_MONTH = 0 - 1;

    private static final Integer DEFAULT_DAY_OF_MONTH = 0;
    private static final Integer UPDATED_DAY_OF_MONTH = 1;
    private static final Integer SMALLER_DAY_OF_MONTH = 0 - 1;

    private static final Integer DEFAULT_MONTH_OF_YEAR = 0;
    private static final Integer UPDATED_MONTH_OF_YEAR = 1;
    private static final Integer SMALLER_MONTH_OF_YEAR = 0 - 1;

    private static final String ENTITY_API_URL = "/api/scheduled-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduledTransactionMockMvc;

    private ScheduledTransaction scheduledTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledTransaction createEntity(EntityManager em) {
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .originalAmount(DEFAULT_ORIGINAL_AMOUNT)
            .movementType(DEFAULT_MOVEMENT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .addToReports(DEFAULT_ADD_TO_REPORTS)
            .recurringType(DEFAULT_RECURRING_TYPE)
            .separationCount(DEFAULT_SEPARATION_COUNT)
            .maxNumberOfOcurrences(DEFAULT_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .weekOfMonth(DEFAULT_WEEK_OF_MONTH)
            .dayOfMonth(DEFAULT_DAY_OF_MONTH)
            .monthOfYear(DEFAULT_MONTH_OF_YEAR);
        return scheduledTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledTransaction createUpdatedEntity(EntityManager em) {
        ScheduledTransaction scheduledTransaction = new ScheduledTransaction()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .originalAmount(UPDATED_ORIGINAL_AMOUNT)
            .movementType(UPDATED_MOVEMENT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .recurringType(UPDATED_RECURRING_TYPE)
            .separationCount(UPDATED_SEPARATION_COUNT)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH)
            .monthOfYear(UPDATED_MONTH_OF_YEAR);
        return scheduledTransaction;
    }

    @BeforeEach
    public void initTest() {
        scheduledTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduledTransaction() throws Exception {
        int databaseSizeBeforeCreate = scheduledTransactionRepository.findAll().size();
        // Create the ScheduledTransaction
        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduledTransaction testScheduledTransaction = scheduledTransactionList.get(scheduledTransactionList.size() - 1);
        assertThat(testScheduledTransaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduledTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testScheduledTransaction.getOriginalAmount()).isEqualTo(DEFAULT_ORIGINAL_AMOUNT);
        assertThat(testScheduledTransaction.getMovementType()).isEqualTo(DEFAULT_MOVEMENT_TYPE);
        assertThat(testScheduledTransaction.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testScheduledTransaction.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testScheduledTransaction.getAddToReports()).isEqualTo(DEFAULT_ADD_TO_REPORTS);
        assertThat(testScheduledTransaction.getRecurringType()).isEqualTo(DEFAULT_RECURRING_TYPE);
        assertThat(testScheduledTransaction.getSeparationCount()).isEqualTo(DEFAULT_SEPARATION_COUNT);
        assertThat(testScheduledTransaction.getMaxNumberOfOcurrences()).isEqualTo(DEFAULT_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testScheduledTransaction.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testScheduledTransaction.getWeekOfMonth()).isEqualTo(DEFAULT_WEEK_OF_MONTH);
        assertThat(testScheduledTransaction.getDayOfMonth()).isEqualTo(DEFAULT_DAY_OF_MONTH);
        assertThat(testScheduledTransaction.getMonthOfYear()).isEqualTo(DEFAULT_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void createScheduledTransactionWithExistingId() throws Exception {
        // Create the ScheduledTransaction with an existing ID
        scheduledTransaction.setId(1L);

        int databaseSizeBeforeCreate = scheduledTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledTransactionRepository.findAll().size();
        // set the field null
        scheduledTransaction.setName(null);

        // Create the ScheduledTransaction, which fails.

        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOriginalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledTransactionRepository.findAll().size();
        // set the field null
        scheduledTransaction.setOriginalAmount(null);

        // Create the ScheduledTransaction, which fails.

        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMovementTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledTransactionRepository.findAll().size();
        // set the field null
        scheduledTransaction.setMovementType(null);

        // Create the ScheduledTransaction, which fails.

        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledTransactionRepository.findAll().size();
        // set the field null
        scheduledTransaction.setStartDate(null);

        // Create the ScheduledTransaction, which fails.

        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddToReportsIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledTransactionRepository.findAll().size();
        // set the field null
        scheduledTransaction.setAddToReports(null);

        // Create the ScheduledTransaction, which fails.

        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecurringTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledTransactionRepository.findAll().size();
        // set the field null
        scheduledTransaction.setRecurringType(null);

        // Create the ScheduledTransaction, which fails.

        restScheduledTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScheduledTransactions() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList
        restScheduledTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].originalAmount").value(hasItem(DEFAULT_ORIGINAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].movementType").value(hasItem(DEFAULT_MOVEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].addToReports").value(hasItem(DEFAULT_ADD_TO_REPORTS.booleanValue())))
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
    void getScheduledTransaction() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get the scheduledTransaction
        restScheduledTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduledTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledTransaction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.originalAmount").value(DEFAULT_ORIGINAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.movementType").value(DEFAULT_MOVEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.addToReports").value(DEFAULT_ADD_TO_REPORTS.booleanValue()))
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
    void getScheduledTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        Long id = scheduledTransaction.getId();

        defaultScheduledTransactionShouldBeFound("id.equals=" + id);
        defaultScheduledTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultScheduledTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultScheduledTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultScheduledTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultScheduledTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where name equals to DEFAULT_NAME
        defaultScheduledTransactionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the scheduledTransactionList where name equals to UPDATED_NAME
        defaultScheduledTransactionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where name not equals to DEFAULT_NAME
        defaultScheduledTransactionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the scheduledTransactionList where name not equals to UPDATED_NAME
        defaultScheduledTransactionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultScheduledTransactionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the scheduledTransactionList where name equals to UPDATED_NAME
        defaultScheduledTransactionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where name is not null
        defaultScheduledTransactionShouldBeFound("name.specified=true");

        // Get all the scheduledTransactionList where name is null
        defaultScheduledTransactionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByNameContainsSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where name contains DEFAULT_NAME
        defaultScheduledTransactionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the scheduledTransactionList where name contains UPDATED_NAME
        defaultScheduledTransactionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where name does not contain DEFAULT_NAME
        defaultScheduledTransactionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the scheduledTransactionList where name does not contain UPDATED_NAME
        defaultScheduledTransactionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where description equals to DEFAULT_DESCRIPTION
        defaultScheduledTransactionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the scheduledTransactionList where description equals to UPDATED_DESCRIPTION
        defaultScheduledTransactionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where description not equals to DEFAULT_DESCRIPTION
        defaultScheduledTransactionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the scheduledTransactionList where description not equals to UPDATED_DESCRIPTION
        defaultScheduledTransactionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultScheduledTransactionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the scheduledTransactionList where description equals to UPDATED_DESCRIPTION
        defaultScheduledTransactionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where description is not null
        defaultScheduledTransactionShouldBeFound("description.specified=true");

        // Get all the scheduledTransactionList where description is null
        defaultScheduledTransactionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where description contains DEFAULT_DESCRIPTION
        defaultScheduledTransactionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the scheduledTransactionList where description contains UPDATED_DESCRIPTION
        defaultScheduledTransactionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where description does not contain DEFAULT_DESCRIPTION
        defaultScheduledTransactionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the scheduledTransactionList where description does not contain UPDATED_DESCRIPTION
        defaultScheduledTransactionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount equals to DEFAULT_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.equals=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount equals to UPDATED_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.equals=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount not equals to DEFAULT_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.notEquals=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount not equals to UPDATED_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.notEquals=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount in DEFAULT_ORIGINAL_AMOUNT or UPDATED_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.in=" + DEFAULT_ORIGINAL_AMOUNT + "," + UPDATED_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount equals to UPDATED_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.in=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount is not null
        defaultScheduledTransactionShouldBeFound("originalAmount.specified=true");

        // Get all the scheduledTransactionList where originalAmount is null
        defaultScheduledTransactionShouldNotBeFound("originalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount is greater than or equal to DEFAULT_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.greaterThanOrEqual=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount is greater than or equal to UPDATED_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.greaterThanOrEqual=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount is less than or equal to DEFAULT_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.lessThanOrEqual=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount is less than or equal to SMALLER_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.lessThanOrEqual=" + SMALLER_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount is less than DEFAULT_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.lessThan=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount is less than UPDATED_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.lessThan=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByOriginalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where originalAmount is greater than DEFAULT_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldNotBeFound("originalAmount.greaterThan=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the scheduledTransactionList where originalAmount is greater than SMALLER_ORIGINAL_AMOUNT
        defaultScheduledTransactionShouldBeFound("originalAmount.greaterThan=" + SMALLER_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMovementTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where movementType equals to DEFAULT_MOVEMENT_TYPE
        defaultScheduledTransactionShouldBeFound("movementType.equals=" + DEFAULT_MOVEMENT_TYPE);

        // Get all the scheduledTransactionList where movementType equals to UPDATED_MOVEMENT_TYPE
        defaultScheduledTransactionShouldNotBeFound("movementType.equals=" + UPDATED_MOVEMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMovementTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where movementType not equals to DEFAULT_MOVEMENT_TYPE
        defaultScheduledTransactionShouldNotBeFound("movementType.notEquals=" + DEFAULT_MOVEMENT_TYPE);

        // Get all the scheduledTransactionList where movementType not equals to UPDATED_MOVEMENT_TYPE
        defaultScheduledTransactionShouldBeFound("movementType.notEquals=" + UPDATED_MOVEMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMovementTypeIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where movementType in DEFAULT_MOVEMENT_TYPE or UPDATED_MOVEMENT_TYPE
        defaultScheduledTransactionShouldBeFound("movementType.in=" + DEFAULT_MOVEMENT_TYPE + "," + UPDATED_MOVEMENT_TYPE);

        // Get all the scheduledTransactionList where movementType equals to UPDATED_MOVEMENT_TYPE
        defaultScheduledTransactionShouldNotBeFound("movementType.in=" + UPDATED_MOVEMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMovementTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where movementType is not null
        defaultScheduledTransactionShouldBeFound("movementType.specified=true");

        // Get all the scheduledTransactionList where movementType is null
        defaultScheduledTransactionShouldNotBeFound("movementType.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate equals to DEFAULT_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the scheduledTransactionList where startDate equals to UPDATED_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate not equals to DEFAULT_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the scheduledTransactionList where startDate not equals to UPDATED_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the scheduledTransactionList where startDate equals to UPDATED_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate is not null
        defaultScheduledTransactionShouldBeFound("startDate.specified=true");

        // Get all the scheduledTransactionList where startDate is null
        defaultScheduledTransactionShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the scheduledTransactionList where startDate is greater than or equal to UPDATED_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate is less than or equal to DEFAULT_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the scheduledTransactionList where startDate is less than or equal to SMALLER_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate is less than DEFAULT_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the scheduledTransactionList where startDate is less than UPDATED_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where startDate is greater than DEFAULT_START_DATE
        defaultScheduledTransactionShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the scheduledTransactionList where startDate is greater than SMALLER_START_DATE
        defaultScheduledTransactionShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate equals to DEFAULT_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the scheduledTransactionList where endDate equals to UPDATED_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate not equals to DEFAULT_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the scheduledTransactionList where endDate not equals to UPDATED_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the scheduledTransactionList where endDate equals to UPDATED_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate is not null
        defaultScheduledTransactionShouldBeFound("endDate.specified=true");

        // Get all the scheduledTransactionList where endDate is null
        defaultScheduledTransactionShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the scheduledTransactionList where endDate is greater than or equal to UPDATED_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate is less than or equal to DEFAULT_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the scheduledTransactionList where endDate is less than or equal to SMALLER_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate is less than DEFAULT_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the scheduledTransactionList where endDate is less than UPDATED_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where endDate is greater than DEFAULT_END_DATE
        defaultScheduledTransactionShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the scheduledTransactionList where endDate is greater than SMALLER_END_DATE
        defaultScheduledTransactionShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByAddToReportsIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where addToReports equals to DEFAULT_ADD_TO_REPORTS
        defaultScheduledTransactionShouldBeFound("addToReports.equals=" + DEFAULT_ADD_TO_REPORTS);

        // Get all the scheduledTransactionList where addToReports equals to UPDATED_ADD_TO_REPORTS
        defaultScheduledTransactionShouldNotBeFound("addToReports.equals=" + UPDATED_ADD_TO_REPORTS);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByAddToReportsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where addToReports not equals to DEFAULT_ADD_TO_REPORTS
        defaultScheduledTransactionShouldNotBeFound("addToReports.notEquals=" + DEFAULT_ADD_TO_REPORTS);

        // Get all the scheduledTransactionList where addToReports not equals to UPDATED_ADD_TO_REPORTS
        defaultScheduledTransactionShouldBeFound("addToReports.notEquals=" + UPDATED_ADD_TO_REPORTS);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByAddToReportsIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where addToReports in DEFAULT_ADD_TO_REPORTS or UPDATED_ADD_TO_REPORTS
        defaultScheduledTransactionShouldBeFound("addToReports.in=" + DEFAULT_ADD_TO_REPORTS + "," + UPDATED_ADD_TO_REPORTS);

        // Get all the scheduledTransactionList where addToReports equals to UPDATED_ADD_TO_REPORTS
        defaultScheduledTransactionShouldNotBeFound("addToReports.in=" + UPDATED_ADD_TO_REPORTS);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByAddToReportsIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where addToReports is not null
        defaultScheduledTransactionShouldBeFound("addToReports.specified=true");

        // Get all the scheduledTransactionList where addToReports is null
        defaultScheduledTransactionShouldNotBeFound("addToReports.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByRecurringTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where recurringType equals to DEFAULT_RECURRING_TYPE
        defaultScheduledTransactionShouldBeFound("recurringType.equals=" + DEFAULT_RECURRING_TYPE);

        // Get all the scheduledTransactionList where recurringType equals to UPDATED_RECURRING_TYPE
        defaultScheduledTransactionShouldNotBeFound("recurringType.equals=" + UPDATED_RECURRING_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByRecurringTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where recurringType not equals to DEFAULT_RECURRING_TYPE
        defaultScheduledTransactionShouldNotBeFound("recurringType.notEquals=" + DEFAULT_RECURRING_TYPE);

        // Get all the scheduledTransactionList where recurringType not equals to UPDATED_RECURRING_TYPE
        defaultScheduledTransactionShouldBeFound("recurringType.notEquals=" + UPDATED_RECURRING_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByRecurringTypeIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where recurringType in DEFAULT_RECURRING_TYPE or UPDATED_RECURRING_TYPE
        defaultScheduledTransactionShouldBeFound("recurringType.in=" + DEFAULT_RECURRING_TYPE + "," + UPDATED_RECURRING_TYPE);

        // Get all the scheduledTransactionList where recurringType equals to UPDATED_RECURRING_TYPE
        defaultScheduledTransactionShouldNotBeFound("recurringType.in=" + UPDATED_RECURRING_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByRecurringTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where recurringType is not null
        defaultScheduledTransactionShouldBeFound("recurringType.specified=true");

        // Get all the scheduledTransactionList where recurringType is null
        defaultScheduledTransactionShouldNotBeFound("recurringType.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount equals to DEFAULT_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.equals=" + DEFAULT_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount equals to UPDATED_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.equals=" + UPDATED_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount not equals to DEFAULT_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.notEquals=" + DEFAULT_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount not equals to UPDATED_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.notEquals=" + UPDATED_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount in DEFAULT_SEPARATION_COUNT or UPDATED_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.in=" + DEFAULT_SEPARATION_COUNT + "," + UPDATED_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount equals to UPDATED_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.in=" + UPDATED_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount is not null
        defaultScheduledTransactionShouldBeFound("separationCount.specified=true");

        // Get all the scheduledTransactionList where separationCount is null
        defaultScheduledTransactionShouldNotBeFound("separationCount.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount is greater than or equal to DEFAULT_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.greaterThanOrEqual=" + DEFAULT_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount is greater than or equal to UPDATED_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.greaterThanOrEqual=" + UPDATED_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount is less than or equal to DEFAULT_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.lessThanOrEqual=" + DEFAULT_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount is less than or equal to SMALLER_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.lessThanOrEqual=" + SMALLER_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount is less than DEFAULT_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.lessThan=" + DEFAULT_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount is less than UPDATED_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.lessThan=" + UPDATED_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySeparationCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where separationCount is greater than DEFAULT_SEPARATION_COUNT
        defaultScheduledTransactionShouldNotBeFound("separationCount.greaterThan=" + DEFAULT_SEPARATION_COUNT);

        // Get all the scheduledTransactionList where separationCount is greater than SMALLER_SEPARATION_COUNT
        defaultScheduledTransactionShouldBeFound("separationCount.greaterThan=" + SMALLER_SEPARATION_COUNT);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences equals to DEFAULT_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.equals=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences equals to UPDATED_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.equals=" + UPDATED_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences not equals to DEFAULT_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.notEquals=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences not equals to UPDATED_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.notEquals=" + UPDATED_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences in DEFAULT_MAX_NUMBER_OF_OCURRENCES or UPDATED_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound(
            "maxNumberOfOcurrences.in=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES + "," + UPDATED_MAX_NUMBER_OF_OCURRENCES
        );

        // Get all the scheduledTransactionList where maxNumberOfOcurrences equals to UPDATED_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.in=" + UPDATED_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is not null
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.specified=true");

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is null
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is greater than or equal to DEFAULT_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.greaterThanOrEqual=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is greater than or equal to UPDATED_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.greaterThanOrEqual=" + UPDATED_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is less than or equal to DEFAULT_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.lessThanOrEqual=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is less than or equal to SMALLER_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.lessThanOrEqual=" + SMALLER_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is less than DEFAULT_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.lessThan=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is less than UPDATED_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.lessThan=" + UPDATED_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMaxNumberOfOcurrencesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is greater than DEFAULT_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldNotBeFound("maxNumberOfOcurrences.greaterThan=" + DEFAULT_MAX_NUMBER_OF_OCURRENCES);

        // Get all the scheduledTransactionList where maxNumberOfOcurrences is greater than SMALLER_MAX_NUMBER_OF_OCURRENCES
        defaultScheduledTransactionShouldBeFound("maxNumberOfOcurrences.greaterThan=" + SMALLER_MAX_NUMBER_OF_OCURRENCES);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek equals to DEFAULT_DAY_OF_WEEK
        defaultScheduledTransactionShouldBeFound("dayOfWeek.equals=" + DEFAULT_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek equals to UPDATED_DAY_OF_WEEK
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.equals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek not equals to DEFAULT_DAY_OF_WEEK
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.notEquals=" + DEFAULT_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek not equals to UPDATED_DAY_OF_WEEK
        defaultScheduledTransactionShouldBeFound("dayOfWeek.notEquals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek in DEFAULT_DAY_OF_WEEK or UPDATED_DAY_OF_WEEK
        defaultScheduledTransactionShouldBeFound("dayOfWeek.in=" + DEFAULT_DAY_OF_WEEK + "," + UPDATED_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek equals to UPDATED_DAY_OF_WEEK
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.in=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek is not null
        defaultScheduledTransactionShouldBeFound("dayOfWeek.specified=true");

        // Get all the scheduledTransactionList where dayOfWeek is null
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek is greater than or equal to DEFAULT_DAY_OF_WEEK
        defaultScheduledTransactionShouldBeFound("dayOfWeek.greaterThanOrEqual=" + DEFAULT_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek is greater than or equal to (DEFAULT_DAY_OF_WEEK + 1)
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.greaterThanOrEqual=" + (DEFAULT_DAY_OF_WEEK + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek is less than or equal to DEFAULT_DAY_OF_WEEK
        defaultScheduledTransactionShouldBeFound("dayOfWeek.lessThanOrEqual=" + DEFAULT_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek is less than or equal to SMALLER_DAY_OF_WEEK
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.lessThanOrEqual=" + SMALLER_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek is less than DEFAULT_DAY_OF_WEEK
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.lessThan=" + DEFAULT_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek is less than (DEFAULT_DAY_OF_WEEK + 1)
        defaultScheduledTransactionShouldBeFound("dayOfWeek.lessThan=" + (DEFAULT_DAY_OF_WEEK + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfWeekIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfWeek is greater than DEFAULT_DAY_OF_WEEK
        defaultScheduledTransactionShouldNotBeFound("dayOfWeek.greaterThan=" + DEFAULT_DAY_OF_WEEK);

        // Get all the scheduledTransactionList where dayOfWeek is greater than SMALLER_DAY_OF_WEEK
        defaultScheduledTransactionShouldBeFound("dayOfWeek.greaterThan=" + SMALLER_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth equals to DEFAULT_WEEK_OF_MONTH
        defaultScheduledTransactionShouldBeFound("weekOfMonth.equals=" + DEFAULT_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth equals to UPDATED_WEEK_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.equals=" + UPDATED_WEEK_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth not equals to DEFAULT_WEEK_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.notEquals=" + DEFAULT_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth not equals to UPDATED_WEEK_OF_MONTH
        defaultScheduledTransactionShouldBeFound("weekOfMonth.notEquals=" + UPDATED_WEEK_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth in DEFAULT_WEEK_OF_MONTH or UPDATED_WEEK_OF_MONTH
        defaultScheduledTransactionShouldBeFound("weekOfMonth.in=" + DEFAULT_WEEK_OF_MONTH + "," + UPDATED_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth equals to UPDATED_WEEK_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.in=" + UPDATED_WEEK_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth is not null
        defaultScheduledTransactionShouldBeFound("weekOfMonth.specified=true");

        // Get all the scheduledTransactionList where weekOfMonth is null
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth is greater than or equal to DEFAULT_WEEK_OF_MONTH
        defaultScheduledTransactionShouldBeFound("weekOfMonth.greaterThanOrEqual=" + DEFAULT_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth is greater than or equal to (DEFAULT_WEEK_OF_MONTH + 1)
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.greaterThanOrEqual=" + (DEFAULT_WEEK_OF_MONTH + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth is less than or equal to DEFAULT_WEEK_OF_MONTH
        defaultScheduledTransactionShouldBeFound("weekOfMonth.lessThanOrEqual=" + DEFAULT_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth is less than or equal to SMALLER_WEEK_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.lessThanOrEqual=" + SMALLER_WEEK_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth is less than DEFAULT_WEEK_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.lessThan=" + DEFAULT_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth is less than (DEFAULT_WEEK_OF_MONTH + 1)
        defaultScheduledTransactionShouldBeFound("weekOfMonth.lessThan=" + (DEFAULT_WEEK_OF_MONTH + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByWeekOfMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where weekOfMonth is greater than DEFAULT_WEEK_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("weekOfMonth.greaterThan=" + DEFAULT_WEEK_OF_MONTH);

        // Get all the scheduledTransactionList where weekOfMonth is greater than SMALLER_WEEK_OF_MONTH
        defaultScheduledTransactionShouldBeFound("weekOfMonth.greaterThan=" + SMALLER_WEEK_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth equals to DEFAULT_DAY_OF_MONTH
        defaultScheduledTransactionShouldBeFound("dayOfMonth.equals=" + DEFAULT_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth equals to UPDATED_DAY_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.equals=" + UPDATED_DAY_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth not equals to DEFAULT_DAY_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.notEquals=" + DEFAULT_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth not equals to UPDATED_DAY_OF_MONTH
        defaultScheduledTransactionShouldBeFound("dayOfMonth.notEquals=" + UPDATED_DAY_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth in DEFAULT_DAY_OF_MONTH or UPDATED_DAY_OF_MONTH
        defaultScheduledTransactionShouldBeFound("dayOfMonth.in=" + DEFAULT_DAY_OF_MONTH + "," + UPDATED_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth equals to UPDATED_DAY_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.in=" + UPDATED_DAY_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth is not null
        defaultScheduledTransactionShouldBeFound("dayOfMonth.specified=true");

        // Get all the scheduledTransactionList where dayOfMonth is null
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth is greater than or equal to DEFAULT_DAY_OF_MONTH
        defaultScheduledTransactionShouldBeFound("dayOfMonth.greaterThanOrEqual=" + DEFAULT_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth is greater than or equal to (DEFAULT_DAY_OF_MONTH + 1)
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.greaterThanOrEqual=" + (DEFAULT_DAY_OF_MONTH + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth is less than or equal to DEFAULT_DAY_OF_MONTH
        defaultScheduledTransactionShouldBeFound("dayOfMonth.lessThanOrEqual=" + DEFAULT_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth is less than or equal to SMALLER_DAY_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.lessThanOrEqual=" + SMALLER_DAY_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth is less than DEFAULT_DAY_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.lessThan=" + DEFAULT_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth is less than (DEFAULT_DAY_OF_MONTH + 1)
        defaultScheduledTransactionShouldBeFound("dayOfMonth.lessThan=" + (DEFAULT_DAY_OF_MONTH + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByDayOfMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where dayOfMonth is greater than DEFAULT_DAY_OF_MONTH
        defaultScheduledTransactionShouldNotBeFound("dayOfMonth.greaterThan=" + DEFAULT_DAY_OF_MONTH);

        // Get all the scheduledTransactionList where dayOfMonth is greater than SMALLER_DAY_OF_MONTH
        defaultScheduledTransactionShouldBeFound("dayOfMonth.greaterThan=" + SMALLER_DAY_OF_MONTH);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear equals to DEFAULT_MONTH_OF_YEAR
        defaultScheduledTransactionShouldBeFound("monthOfYear.equals=" + DEFAULT_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear equals to UPDATED_MONTH_OF_YEAR
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.equals=" + UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear not equals to DEFAULT_MONTH_OF_YEAR
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.notEquals=" + DEFAULT_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear not equals to UPDATED_MONTH_OF_YEAR
        defaultScheduledTransactionShouldBeFound("monthOfYear.notEquals=" + UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear in DEFAULT_MONTH_OF_YEAR or UPDATED_MONTH_OF_YEAR
        defaultScheduledTransactionShouldBeFound("monthOfYear.in=" + DEFAULT_MONTH_OF_YEAR + "," + UPDATED_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear equals to UPDATED_MONTH_OF_YEAR
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.in=" + UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear is not null
        defaultScheduledTransactionShouldBeFound("monthOfYear.specified=true");

        // Get all the scheduledTransactionList where monthOfYear is null
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear is greater than or equal to DEFAULT_MONTH_OF_YEAR
        defaultScheduledTransactionShouldBeFound("monthOfYear.greaterThanOrEqual=" + DEFAULT_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear is greater than or equal to (DEFAULT_MONTH_OF_YEAR + 1)
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.greaterThanOrEqual=" + (DEFAULT_MONTH_OF_YEAR + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear is less than or equal to DEFAULT_MONTH_OF_YEAR
        defaultScheduledTransactionShouldBeFound("monthOfYear.lessThanOrEqual=" + DEFAULT_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear is less than or equal to SMALLER_MONTH_OF_YEAR
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.lessThanOrEqual=" + SMALLER_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear is less than DEFAULT_MONTH_OF_YEAR
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.lessThan=" + DEFAULT_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear is less than (DEFAULT_MONTH_OF_YEAR + 1)
        defaultScheduledTransactionShouldBeFound("monthOfYear.lessThan=" + (DEFAULT_MONTH_OF_YEAR + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByMonthOfYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        // Get all the scheduledTransactionList where monthOfYear is greater than DEFAULT_MONTH_OF_YEAR
        defaultScheduledTransactionShouldNotBeFound("monthOfYear.greaterThan=" + DEFAULT_MONTH_OF_YEAR);

        // Get all the scheduledTransactionList where monthOfYear is greater than SMALLER_MONTH_OF_YEAR
        defaultScheduledTransactionShouldBeFound("monthOfYear.greaterThan=" + SMALLER_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);
        Currency currency = CurrencyResourceIT.createEntity(em);
        em.persist(currency);
        em.flush();
        scheduledTransaction.setCurrency(currency);
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);
        Long currencyId = currency.getId();

        // Get all the scheduledTransactionList where currency equals to currencyId
        defaultScheduledTransactionShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the scheduledTransactionList where currency equals to (currencyId + 1)
        defaultScheduledTransactionShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsBySourceUserIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);
        UserDetails sourceUser = UserDetailsResourceIT.createEntity(em);
        em.persist(sourceUser);
        em.flush();
        scheduledTransaction.setSourceUser(sourceUser);
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);
        Long sourceUserId = sourceUser.getId();

        // Get all the scheduledTransactionList where sourceUser equals to sourceUserId
        defaultScheduledTransactionShouldBeFound("sourceUserId.equals=" + sourceUserId);

        // Get all the scheduledTransactionList where sourceUser equals to (sourceUserId + 1)
        defaultScheduledTransactionShouldNotBeFound("sourceUserId.equals=" + (sourceUserId + 1));
    }

    @Test
    @Transactional
    void getAllScheduledTransactionsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        scheduledTransaction.setCategory(category);
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);
        Long categoryId = category.getId();

        // Get all the scheduledTransactionList where category equals to categoryId
        defaultScheduledTransactionShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the scheduledTransactionList where category equals to (categoryId + 1)
        defaultScheduledTransactionShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScheduledTransactionShouldBeFound(String filter) throws Exception {
        restScheduledTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].originalAmount").value(hasItem(DEFAULT_ORIGINAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].movementType").value(hasItem(DEFAULT_MOVEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].addToReports").value(hasItem(DEFAULT_ADD_TO_REPORTS.booleanValue())))
            .andExpect(jsonPath("$.[*].recurringType").value(hasItem(DEFAULT_RECURRING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].separationCount").value(hasItem(DEFAULT_SEPARATION_COUNT)))
            .andExpect(jsonPath("$.[*].maxNumberOfOcurrences").value(hasItem(DEFAULT_MAX_NUMBER_OF_OCURRENCES)))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].weekOfMonth").value(hasItem(DEFAULT_WEEK_OF_MONTH)))
            .andExpect(jsonPath("$.[*].dayOfMonth").value(hasItem(DEFAULT_DAY_OF_MONTH)))
            .andExpect(jsonPath("$.[*].monthOfYear").value(hasItem(DEFAULT_MONTH_OF_YEAR)));

        // Check, that the count call also returns 1
        restScheduledTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScheduledTransactionShouldNotBeFound(String filter) throws Exception {
        restScheduledTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScheduledTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScheduledTransaction() throws Exception {
        // Get the scheduledTransaction
        restScheduledTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduledTransaction() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();

        // Update the scheduledTransaction
        ScheduledTransaction updatedScheduledTransaction = scheduledTransactionRepository.findById(scheduledTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedScheduledTransaction are not directly saved in db
        em.detach(updatedScheduledTransaction);
        updatedScheduledTransaction
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .originalAmount(UPDATED_ORIGINAL_AMOUNT)
            .movementType(UPDATED_MOVEMENT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .recurringType(UPDATED_RECURRING_TYPE)
            .separationCount(UPDATED_SEPARATION_COUNT)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH)
            .monthOfYear(UPDATED_MONTH_OF_YEAR);

        restScheduledTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduledTransaction.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduledTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
        ScheduledTransaction testScheduledTransaction = scheduledTransactionList.get(scheduledTransactionList.size() - 1);
        assertThat(testScheduledTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduledTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testScheduledTransaction.getOriginalAmount()).isEqualTo(UPDATED_ORIGINAL_AMOUNT);
        assertThat(testScheduledTransaction.getMovementType()).isEqualTo(UPDATED_MOVEMENT_TYPE);
        assertThat(testScheduledTransaction.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testScheduledTransaction.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testScheduledTransaction.getAddToReports()).isEqualTo(UPDATED_ADD_TO_REPORTS);
        assertThat(testScheduledTransaction.getRecurringType()).isEqualTo(UPDATED_RECURRING_TYPE);
        assertThat(testScheduledTransaction.getSeparationCount()).isEqualTo(UPDATED_SEPARATION_COUNT);
        assertThat(testScheduledTransaction.getMaxNumberOfOcurrences()).isEqualTo(UPDATED_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testScheduledTransaction.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testScheduledTransaction.getWeekOfMonth()).isEqualTo(UPDATED_WEEK_OF_MONTH);
        assertThat(testScheduledTransaction.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testScheduledTransaction.getMonthOfYear()).isEqualTo(UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingScheduledTransaction() throws Exception {
        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();
        scheduledTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduledTransaction.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduledTransaction() throws Exception {
        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();
        scheduledTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduledTransaction() throws Exception {
        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();
        scheduledTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduledTransactionWithPatch() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();

        // Update the scheduledTransaction using partial update
        ScheduledTransaction partialUpdatedScheduledTransaction = new ScheduledTransaction();
        partialUpdatedScheduledTransaction.setId(scheduledTransaction.getId());

        partialUpdatedScheduledTransaction
            .name(UPDATED_NAME)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .recurringType(UPDATED_RECURRING_TYPE)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfMonth(UPDATED_DAY_OF_MONTH);

        restScheduledTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduledTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
        ScheduledTransaction testScheduledTransaction = scheduledTransactionList.get(scheduledTransactionList.size() - 1);
        assertThat(testScheduledTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduledTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testScheduledTransaction.getOriginalAmount()).isEqualTo(DEFAULT_ORIGINAL_AMOUNT);
        assertThat(testScheduledTransaction.getMovementType()).isEqualTo(DEFAULT_MOVEMENT_TYPE);
        assertThat(testScheduledTransaction.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testScheduledTransaction.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testScheduledTransaction.getAddToReports()).isEqualTo(UPDATED_ADD_TO_REPORTS);
        assertThat(testScheduledTransaction.getRecurringType()).isEqualTo(UPDATED_RECURRING_TYPE);
        assertThat(testScheduledTransaction.getSeparationCount()).isEqualTo(DEFAULT_SEPARATION_COUNT);
        assertThat(testScheduledTransaction.getMaxNumberOfOcurrences()).isEqualTo(UPDATED_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testScheduledTransaction.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testScheduledTransaction.getWeekOfMonth()).isEqualTo(DEFAULT_WEEK_OF_MONTH);
        assertThat(testScheduledTransaction.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testScheduledTransaction.getMonthOfYear()).isEqualTo(DEFAULT_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateScheduledTransactionWithPatch() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();

        // Update the scheduledTransaction using partial update
        ScheduledTransaction partialUpdatedScheduledTransaction = new ScheduledTransaction();
        partialUpdatedScheduledTransaction.setId(scheduledTransaction.getId());

        partialUpdatedScheduledTransaction
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .originalAmount(UPDATED_ORIGINAL_AMOUNT)
            .movementType(UPDATED_MOVEMENT_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .recurringType(UPDATED_RECURRING_TYPE)
            .separationCount(UPDATED_SEPARATION_COUNT)
            .maxNumberOfOcurrences(UPDATED_MAX_NUMBER_OF_OCURRENCES)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .weekOfMonth(UPDATED_WEEK_OF_MONTH)
            .dayOfMonth(UPDATED_DAY_OF_MONTH)
            .monthOfYear(UPDATED_MONTH_OF_YEAR);

        restScheduledTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduledTransaction))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
        ScheduledTransaction testScheduledTransaction = scheduledTransactionList.get(scheduledTransactionList.size() - 1);
        assertThat(testScheduledTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduledTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testScheduledTransaction.getOriginalAmount()).isEqualTo(UPDATED_ORIGINAL_AMOUNT);
        assertThat(testScheduledTransaction.getMovementType()).isEqualTo(UPDATED_MOVEMENT_TYPE);
        assertThat(testScheduledTransaction.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testScheduledTransaction.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testScheduledTransaction.getAddToReports()).isEqualTo(UPDATED_ADD_TO_REPORTS);
        assertThat(testScheduledTransaction.getRecurringType()).isEqualTo(UPDATED_RECURRING_TYPE);
        assertThat(testScheduledTransaction.getSeparationCount()).isEqualTo(UPDATED_SEPARATION_COUNT);
        assertThat(testScheduledTransaction.getMaxNumberOfOcurrences()).isEqualTo(UPDATED_MAX_NUMBER_OF_OCURRENCES);
        assertThat(testScheduledTransaction.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testScheduledTransaction.getWeekOfMonth()).isEqualTo(UPDATED_WEEK_OF_MONTH);
        assertThat(testScheduledTransaction.getDayOfMonth()).isEqualTo(UPDATED_DAY_OF_MONTH);
        assertThat(testScheduledTransaction.getMonthOfYear()).isEqualTo(UPDATED_MONTH_OF_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingScheduledTransaction() throws Exception {
        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();
        scheduledTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduledTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduledTransaction() throws Exception {
        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();
        scheduledTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduledTransaction() throws Exception {
        int databaseSizeBeforeUpdate = scheduledTransactionRepository.findAll().size();
        scheduledTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduledTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledTransaction in the database
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduledTransaction() throws Exception {
        // Initialize the database
        scheduledTransactionRepository.saveAndFlush(scheduledTransaction);

        int databaseSizeBeforeDelete = scheduledTransactionRepository.findAll().size();

        // Delete the scheduledTransaction
        restScheduledTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduledTransaction.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduledTransaction> scheduledTransactionList = scheduledTransactionRepository.findAll();
        assertThat(scheduledTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
