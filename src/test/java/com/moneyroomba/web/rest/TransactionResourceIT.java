package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Attachment;
import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.service.criteria.TransactionCriteria;
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
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ADDED = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;
    private static final Double SMALLER_AMOUNT = 0D - 1D;

    private static final Double DEFAULT_ORIGINAL_AMOUNT = 0D;
    private static final Double UPDATED_ORIGINAL_AMOUNT = 1D;
    private static final Double SMALLER_ORIGINAL_AMOUNT = 0D - 1D;

    private static final MovementType DEFAULT_MOVEMENT_TYPE = MovementType.INCOME;
    private static final MovementType UPDATED_MOVEMENT_TYPE = MovementType.EXPENSE;

    private static final Boolean DEFAULT_SCHEDULED = false;
    private static final Boolean UPDATED_SCHEDULED = true;

    private static final Boolean DEFAULT_ADD_TO_REPORTS = false;
    private static final Boolean UPDATED_ADD_TO_REPORTS = true;

    private static final Boolean DEFAULT_INCOMING_TRANSACTION = false;
    private static final Boolean UPDATED_INCOMING_TRANSACTION = true;

    private static final TransactionType DEFAULT_TRANSACTION_TYPE = TransactionType.MANUAL;
    private static final TransactionType UPDATED_TRANSACTION_TYPE = TransactionType.SCHEDULED;

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .dateAdded(DEFAULT_DATE_ADDED)
            .amount(DEFAULT_AMOUNT)
            .originalAmount(DEFAULT_ORIGINAL_AMOUNT)
            .movementType(DEFAULT_MOVEMENT_TYPE)
            .scheduled(DEFAULT_SCHEDULED)
            .addToReports(DEFAULT_ADD_TO_REPORTS)
            .incomingTransaction(DEFAULT_INCOMING_TRANSACTION)
            .transactionType(DEFAULT_TRANSACTION_TYPE);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .dateAdded(UPDATED_DATE_ADDED)
            .amount(UPDATED_AMOUNT)
            .originalAmount(UPDATED_ORIGINAL_AMOUNT)
            .movementType(UPDATED_MOVEMENT_TYPE)
            .scheduled(UPDATED_SCHEDULED)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .incomingTransaction(UPDATED_INCOMING_TRANSACTION)
            .transactionType(UPDATED_TRANSACTION_TYPE);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();
        // Create the Transaction
        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransaction.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getOriginalAmount()).isEqualTo(DEFAULT_ORIGINAL_AMOUNT);
        assertThat(testTransaction.getMovementType()).isEqualTo(DEFAULT_MOVEMENT_TYPE);
        assertThat(testTransaction.getScheduled()).isEqualTo(DEFAULT_SCHEDULED);
        assertThat(testTransaction.getAddToReports()).isEqualTo(DEFAULT_ADD_TO_REPORTS);
        assertThat(testTransaction.getIncomingTransaction()).isEqualTo(DEFAULT_INCOMING_TRANSACTION);
        assertThat(testTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);

        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setName(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setDateAdded(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOriginalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setOriginalAmount(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMovementTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setMovementType(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduledIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setScheduled(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddToReportsIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setAddToReports(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncomingTransactionIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setIncomingTransaction(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactionType(null);

        // Create the Transaction, which fails.

        restTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].originalAmount").value(hasItem(DEFAULT_ORIGINAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].movementType").value(hasItem(DEFAULT_MOVEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].scheduled").value(hasItem(DEFAULT_SCHEDULED.booleanValue())))
            .andExpect(jsonPath("$.[*].addToReports").value(hasItem(DEFAULT_ADD_TO_REPORTS.booleanValue())))
            .andExpect(jsonPath("$.[*].incomingTransaction").value(hasItem(DEFAULT_INCOMING_TRANSACTION.booleanValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.originalAmount").value(DEFAULT_ORIGINAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.movementType").value(DEFAULT_MOVEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.scheduled").value(DEFAULT_SCHEDULED.booleanValue()))
            .andExpect(jsonPath("$.addToReports").value(DEFAULT_ADD_TO_REPORTS.booleanValue()))
            .andExpect(jsonPath("$.incomingTransaction").value(DEFAULT_INCOMING_TRANSACTION.booleanValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        Long id = transaction.getId();

        defaultTransactionShouldBeFound("id.equals=" + id);
        defaultTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where name equals to DEFAULT_NAME
        defaultTransactionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the transactionList where name equals to UPDATED_NAME
        defaultTransactionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where name not equals to DEFAULT_NAME
        defaultTransactionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the transactionList where name not equals to UPDATED_NAME
        defaultTransactionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTransactionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the transactionList where name equals to UPDATED_NAME
        defaultTransactionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where name is not null
        defaultTransactionShouldBeFound("name.specified=true");

        // Get all the transactionList where name is null
        defaultTransactionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByNameContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where name contains DEFAULT_NAME
        defaultTransactionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the transactionList where name contains UPDATED_NAME
        defaultTransactionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where name does not contain DEFAULT_NAME
        defaultTransactionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the transactionList where name does not contain UPDATED_NAME
        defaultTransactionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description equals to DEFAULT_DESCRIPTION
        defaultTransactionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionList where description equals to UPDATED_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description not equals to DEFAULT_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionList where description not equals to UPDATED_DESCRIPTION
        defaultTransactionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTransactionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the transactionList where description equals to UPDATED_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description is not null
        defaultTransactionShouldBeFound("description.specified=true");

        // Get all the transactionList where description is null
        defaultTransactionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description contains DEFAULT_DESCRIPTION
        defaultTransactionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the transactionList where description contains UPDATED_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description does not contain DEFAULT_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the transactionList where description does not contain UPDATED_DESCRIPTION
        defaultTransactionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded equals to DEFAULT_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.equals=" + DEFAULT_DATE_ADDED);

        // Get all the transactionList where dateAdded equals to UPDATED_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.equals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded not equals to DEFAULT_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.notEquals=" + DEFAULT_DATE_ADDED);

        // Get all the transactionList where dateAdded not equals to UPDATED_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.notEquals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded in DEFAULT_DATE_ADDED or UPDATED_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.in=" + DEFAULT_DATE_ADDED + "," + UPDATED_DATE_ADDED);

        // Get all the transactionList where dateAdded equals to UPDATED_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.in=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded is not null
        defaultTransactionShouldBeFound("dateAdded.specified=true");

        // Get all the transactionList where dateAdded is null
        defaultTransactionShouldNotBeFound("dateAdded.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded is greater than or equal to DEFAULT_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.greaterThanOrEqual=" + DEFAULT_DATE_ADDED);

        // Get all the transactionList where dateAdded is greater than or equal to UPDATED_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.greaterThanOrEqual=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded is less than or equal to DEFAULT_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.lessThanOrEqual=" + DEFAULT_DATE_ADDED);

        // Get all the transactionList where dateAdded is less than or equal to SMALLER_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.lessThanOrEqual=" + SMALLER_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded is less than DEFAULT_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.lessThan=" + DEFAULT_DATE_ADDED);

        // Get all the transactionList where dateAdded is less than UPDATED_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.lessThan=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateAddedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where dateAdded is greater than DEFAULT_DATE_ADDED
        defaultTransactionShouldNotBeFound("dateAdded.greaterThan=" + DEFAULT_DATE_ADDED);

        // Get all the transactionList where dateAdded is greater than SMALLER_DATE_ADDED
        defaultTransactionShouldBeFound("dateAdded.greaterThan=" + SMALLER_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount equals to DEFAULT_AMOUNT
        defaultTransactionShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount equals to UPDATED_AMOUNT
        defaultTransactionShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount not equals to DEFAULT_AMOUNT
        defaultTransactionShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount not equals to UPDATED_AMOUNT
        defaultTransactionShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionList where amount equals to UPDATED_AMOUNT
        defaultTransactionShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is not null
        defaultTransactionShouldBeFound("amount.specified=true");

        // Get all the transactionList where amount is null
        defaultTransactionShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultTransactionShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount is greater than or equal to UPDATED_AMOUNT
        defaultTransactionShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is less than or equal to DEFAULT_AMOUNT
        defaultTransactionShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount is less than or equal to SMALLER_AMOUNT
        defaultTransactionShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is less than DEFAULT_AMOUNT
        defaultTransactionShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount is less than UPDATED_AMOUNT
        defaultTransactionShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where amount is greater than DEFAULT_AMOUNT
        defaultTransactionShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the transactionList where amount is greater than SMALLER_AMOUNT
        defaultTransactionShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount equals to DEFAULT_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.equals=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount equals to UPDATED_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.equals=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount not equals to DEFAULT_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.notEquals=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount not equals to UPDATED_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.notEquals=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount in DEFAULT_ORIGINAL_AMOUNT or UPDATED_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.in=" + DEFAULT_ORIGINAL_AMOUNT + "," + UPDATED_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount equals to UPDATED_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.in=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount is not null
        defaultTransactionShouldBeFound("originalAmount.specified=true");

        // Get all the transactionList where originalAmount is null
        defaultTransactionShouldNotBeFound("originalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount is greater than or equal to DEFAULT_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.greaterThanOrEqual=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount is greater than or equal to UPDATED_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.greaterThanOrEqual=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount is less than or equal to DEFAULT_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.lessThanOrEqual=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount is less than or equal to SMALLER_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.lessThanOrEqual=" + SMALLER_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount is less than DEFAULT_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.lessThan=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount is less than UPDATED_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.lessThan=" + UPDATED_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByOriginalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where originalAmount is greater than DEFAULT_ORIGINAL_AMOUNT
        defaultTransactionShouldNotBeFound("originalAmount.greaterThan=" + DEFAULT_ORIGINAL_AMOUNT);

        // Get all the transactionList where originalAmount is greater than SMALLER_ORIGINAL_AMOUNT
        defaultTransactionShouldBeFound("originalAmount.greaterThan=" + SMALLER_ORIGINAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByMovementTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where movementType equals to DEFAULT_MOVEMENT_TYPE
        defaultTransactionShouldBeFound("movementType.equals=" + DEFAULT_MOVEMENT_TYPE);

        // Get all the transactionList where movementType equals to UPDATED_MOVEMENT_TYPE
        defaultTransactionShouldNotBeFound("movementType.equals=" + UPDATED_MOVEMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByMovementTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where movementType not equals to DEFAULT_MOVEMENT_TYPE
        defaultTransactionShouldNotBeFound("movementType.notEquals=" + DEFAULT_MOVEMENT_TYPE);

        // Get all the transactionList where movementType not equals to UPDATED_MOVEMENT_TYPE
        defaultTransactionShouldBeFound("movementType.notEquals=" + UPDATED_MOVEMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByMovementTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where movementType in DEFAULT_MOVEMENT_TYPE or UPDATED_MOVEMENT_TYPE
        defaultTransactionShouldBeFound("movementType.in=" + DEFAULT_MOVEMENT_TYPE + "," + UPDATED_MOVEMENT_TYPE);

        // Get all the transactionList where movementType equals to UPDATED_MOVEMENT_TYPE
        defaultTransactionShouldNotBeFound("movementType.in=" + UPDATED_MOVEMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByMovementTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where movementType is not null
        defaultTransactionShouldBeFound("movementType.specified=true");

        // Get all the transactionList where movementType is null
        defaultTransactionShouldNotBeFound("movementType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByScheduledIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where scheduled equals to DEFAULT_SCHEDULED
        defaultTransactionShouldBeFound("scheduled.equals=" + DEFAULT_SCHEDULED);

        // Get all the transactionList where scheduled equals to UPDATED_SCHEDULED
        defaultTransactionShouldNotBeFound("scheduled.equals=" + UPDATED_SCHEDULED);
    }

    @Test
    @Transactional
    void getAllTransactionsByScheduledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where scheduled not equals to DEFAULT_SCHEDULED
        defaultTransactionShouldNotBeFound("scheduled.notEquals=" + DEFAULT_SCHEDULED);

        // Get all the transactionList where scheduled not equals to UPDATED_SCHEDULED
        defaultTransactionShouldBeFound("scheduled.notEquals=" + UPDATED_SCHEDULED);
    }

    @Test
    @Transactional
    void getAllTransactionsByScheduledIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where scheduled in DEFAULT_SCHEDULED or UPDATED_SCHEDULED
        defaultTransactionShouldBeFound("scheduled.in=" + DEFAULT_SCHEDULED + "," + UPDATED_SCHEDULED);

        // Get all the transactionList where scheduled equals to UPDATED_SCHEDULED
        defaultTransactionShouldNotBeFound("scheduled.in=" + UPDATED_SCHEDULED);
    }

    @Test
    @Transactional
    void getAllTransactionsByScheduledIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where scheduled is not null
        defaultTransactionShouldBeFound("scheduled.specified=true");

        // Get all the transactionList where scheduled is null
        defaultTransactionShouldNotBeFound("scheduled.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByAddToReportsIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where addToReports equals to DEFAULT_ADD_TO_REPORTS
        defaultTransactionShouldBeFound("addToReports.equals=" + DEFAULT_ADD_TO_REPORTS);

        // Get all the transactionList where addToReports equals to UPDATED_ADD_TO_REPORTS
        defaultTransactionShouldNotBeFound("addToReports.equals=" + UPDATED_ADD_TO_REPORTS);
    }

    @Test
    @Transactional
    void getAllTransactionsByAddToReportsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where addToReports not equals to DEFAULT_ADD_TO_REPORTS
        defaultTransactionShouldNotBeFound("addToReports.notEquals=" + DEFAULT_ADD_TO_REPORTS);

        // Get all the transactionList where addToReports not equals to UPDATED_ADD_TO_REPORTS
        defaultTransactionShouldBeFound("addToReports.notEquals=" + UPDATED_ADD_TO_REPORTS);
    }

    @Test
    @Transactional
    void getAllTransactionsByAddToReportsIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where addToReports in DEFAULT_ADD_TO_REPORTS or UPDATED_ADD_TO_REPORTS
        defaultTransactionShouldBeFound("addToReports.in=" + DEFAULT_ADD_TO_REPORTS + "," + UPDATED_ADD_TO_REPORTS);

        // Get all the transactionList where addToReports equals to UPDATED_ADD_TO_REPORTS
        defaultTransactionShouldNotBeFound("addToReports.in=" + UPDATED_ADD_TO_REPORTS);
    }

    @Test
    @Transactional
    void getAllTransactionsByAddToReportsIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where addToReports is not null
        defaultTransactionShouldBeFound("addToReports.specified=true");

        // Get all the transactionList where addToReports is null
        defaultTransactionShouldNotBeFound("addToReports.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByIncomingTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where incomingTransaction equals to DEFAULT_INCOMING_TRANSACTION
        defaultTransactionShouldBeFound("incomingTransaction.equals=" + DEFAULT_INCOMING_TRANSACTION);

        // Get all the transactionList where incomingTransaction equals to UPDATED_INCOMING_TRANSACTION
        defaultTransactionShouldNotBeFound("incomingTransaction.equals=" + UPDATED_INCOMING_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByIncomingTransactionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where incomingTransaction not equals to DEFAULT_INCOMING_TRANSACTION
        defaultTransactionShouldNotBeFound("incomingTransaction.notEquals=" + DEFAULT_INCOMING_TRANSACTION);

        // Get all the transactionList where incomingTransaction not equals to UPDATED_INCOMING_TRANSACTION
        defaultTransactionShouldBeFound("incomingTransaction.notEquals=" + UPDATED_INCOMING_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByIncomingTransactionIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where incomingTransaction in DEFAULT_INCOMING_TRANSACTION or UPDATED_INCOMING_TRANSACTION
        defaultTransactionShouldBeFound("incomingTransaction.in=" + DEFAULT_INCOMING_TRANSACTION + "," + UPDATED_INCOMING_TRANSACTION);

        // Get all the transactionList where incomingTransaction equals to UPDATED_INCOMING_TRANSACTION
        defaultTransactionShouldNotBeFound("incomingTransaction.in=" + UPDATED_INCOMING_TRANSACTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByIncomingTransactionIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where incomingTransaction is not null
        defaultTransactionShouldBeFound("incomingTransaction.specified=true");

        // Get all the transactionList where incomingTransaction is null
        defaultTransactionShouldNotBeFound("incomingTransaction.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTransactionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionType not equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionShouldNotBeFound("transactionType.notEquals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionList where transactionType not equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionShouldBeFound("transactionType.notEquals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultTransactionShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the transactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionType is not null
        defaultTransactionShouldBeFound("transactionType.specified=true");

        // Get all the transactionList where transactionType is null
        defaultTransactionShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Attachment attachment = AttachmentResourceIT.createEntity(em);
        em.persist(attachment);
        em.flush();
        transaction.setAttachment(attachment);
        transactionRepository.saveAndFlush(transaction);
        Long attachmentId = attachment.getId();

        // Get all the transactionList where attachment equals to attachmentId
        defaultTransactionShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the transactionList where attachment equals to (attachmentId + 1)
        defaultTransactionShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Wallet wallet = WalletResourceIT.createEntity(em);
        em.persist(wallet);
        em.flush();
        transaction.setWallet(wallet);
        transactionRepository.saveAndFlush(transaction);
        Long walletId = wallet.getId();

        // Get all the transactionList where wallet equals to walletId
        defaultTransactionShouldBeFound("walletId.equals=" + walletId);

        // Get all the transactionList where wallet equals to (walletId + 1)
        defaultTransactionShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Currency currency = CurrencyResourceIT.createEntity(em);
        em.persist(currency);
        em.flush();
        transaction.setCurrency(currency);
        transactionRepository.saveAndFlush(transaction);
        Long currencyId = currency.getId();

        // Get all the transactionList where currency equals to currencyId
        defaultTransactionShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the transactionList where currency equals to (currencyId + 1)
        defaultTransactionShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        transaction.setCategory(category);
        transactionRepository.saveAndFlush(transaction);
        Long categoryId = category.getId();

        // Get all the transactionList where category equals to categoryId
        defaultTransactionShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the transactionList where category equals to (categoryId + 1)
        defaultTransactionShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsBySourceUserIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        UserDetails sourceUser = UserDetailsResourceIT.createEntity(em);
        em.persist(sourceUser);
        em.flush();
        transaction.setSourceUser(sourceUser);
        transactionRepository.saveAndFlush(transaction);
        Long sourceUserId = sourceUser.getId();

        // Get all the transactionList where sourceUser equals to sourceUserId
        defaultTransactionShouldBeFound("sourceUserId.equals=" + sourceUserId);

        // Get all the transactionList where sourceUser equals to (sourceUserId + 1)
        defaultTransactionShouldNotBeFound("sourceUserId.equals=" + (sourceUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionShouldBeFound(String filter) throws Exception {
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].originalAmount").value(hasItem(DEFAULT_ORIGINAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].movementType").value(hasItem(DEFAULT_MOVEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].scheduled").value(hasItem(DEFAULT_SCHEDULED.booleanValue())))
            .andExpect(jsonPath("$.[*].addToReports").value(hasItem(DEFAULT_ADD_TO_REPORTS.booleanValue())))
            .andExpect(jsonPath("$.[*].incomingTransaction").value(hasItem(DEFAULT_INCOMING_TRANSACTION.booleanValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())));

        // Check, that the count call also returns 1
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionShouldNotBeFound(String filter) throws Exception {
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .dateAdded(UPDATED_DATE_ADDED)
            .amount(UPDATED_AMOUNT)
            .originalAmount(UPDATED_ORIGINAL_AMOUNT)
            .movementType(UPDATED_MOVEMENT_TYPE)
            .scheduled(UPDATED_SCHEDULED)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .incomingTransaction(UPDATED_INCOMING_TRANSACTION)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransaction.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransaction.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getOriginalAmount()).isEqualTo(UPDATED_ORIGINAL_AMOUNT);
        assertThat(testTransaction.getMovementType()).isEqualTo(UPDATED_MOVEMENT_TYPE);
        assertThat(testTransaction.getScheduled()).isEqualTo(UPDATED_SCHEDULED);
        assertThat(testTransaction.getAddToReports()).isEqualTo(UPDATED_ADD_TO_REPORTS);
        assertThat(testTransaction.getIncomingTransaction()).isEqualTo(UPDATED_INCOMING_TRANSACTION);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaction.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .description(UPDATED_DESCRIPTION)
            .incomingTransaction(UPDATED_INCOMING_TRANSACTION)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransaction.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getOriginalAmount()).isEqualTo(DEFAULT_ORIGINAL_AMOUNT);
        assertThat(testTransaction.getMovementType()).isEqualTo(DEFAULT_MOVEMENT_TYPE);
        assertThat(testTransaction.getScheduled()).isEqualTo(DEFAULT_SCHEDULED);
        assertThat(testTransaction.getAddToReports()).isEqualTo(DEFAULT_ADD_TO_REPORTS);
        assertThat(testTransaction.getIncomingTransaction()).isEqualTo(UPDATED_INCOMING_TRANSACTION);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .dateAdded(UPDATED_DATE_ADDED)
            .amount(UPDATED_AMOUNT)
            .originalAmount(UPDATED_ORIGINAL_AMOUNT)
            .movementType(UPDATED_MOVEMENT_TYPE)
            .scheduled(UPDATED_SCHEDULED)
            .addToReports(UPDATED_ADD_TO_REPORTS)
            .incomingTransaction(UPDATED_INCOMING_TRANSACTION)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransaction.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getOriginalAmount()).isEqualTo(UPDATED_ORIGINAL_AMOUNT);
        assertThat(testTransaction.getMovementType()).isEqualTo(UPDATED_MOVEMENT_TYPE);
        assertThat(testTransaction.getScheduled()).isEqualTo(UPDATED_SCHEDULED);
        assertThat(testTransaction.getAddToReports()).isEqualTo(UPDATED_ADD_TO_REPORTS);
        assertThat(testTransaction.getIncomingTransaction()).isEqualTo(UPDATED_INCOMING_TRANSACTION);
        assertThat(testTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transaction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaction.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
