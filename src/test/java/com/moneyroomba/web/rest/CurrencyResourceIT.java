package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Invoice;
import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.service.criteria.CurrencyCriteria;
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
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurrencyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_CONVERSION_RATE = 1F;
    private static final Float UPDATED_CONVERSION_RATE = 2F;
    private static final Float SMALLER_CONVERSION_RATE = 1F - 1F;

    private static final String DEFAULT_SYMBOL = "AAAAA";
    private static final String UPDATED_SYMBOL = "BBBBB";

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .conversionRate(DEFAULT_CONVERSION_RATE)
            .symbol(DEFAULT_SYMBOL);
        return currency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity(EntityManager em) {
        Currency currency = new Currency()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .conversionRate(UPDATED_CONVERSION_RATE)
            .symbol(UPDATED_SYMBOL);
        return currency;
    }

    @BeforeEach
    public void initTest() {
        currency = createEntity(em);
    }

    @Test
    @Transactional
    void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();
        // Create the Currency
        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCurrency.getConversionRate()).isEqualTo(DEFAULT_CONVERSION_RATE);
        assertThat(testCurrency.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
    }

    @Test
    @Transactional
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);

        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCode(null);

        // Create the Currency, which fails.

        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setName(null);

        // Create the Currency, which fails.

        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConversionRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setConversionRate(null);

        // Create the Currency, which fails.

        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setSymbol(null);

        // Create the Currency, which fails.

        restCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCurrencies() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].conversionRate").value(hasItem(DEFAULT_CONVERSION_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)));
    }

    @Test
    @Transactional
    void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.conversionRate").value(DEFAULT_CONVERSION_RATE.doubleValue()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL));
    }

    @Test
    @Transactional
    void getCurrenciesByIdFiltering() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        Long id = currency.getId();

        defaultCurrencyShouldBeFound("id.equals=" + id);
        defaultCurrencyShouldNotBeFound("id.notEquals=" + id);

        defaultCurrencyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCurrencyShouldNotBeFound("id.greaterThan=" + id);

        defaultCurrencyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCurrencyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code equals to DEFAULT_CODE
        defaultCurrencyShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the currencyList where code equals to UPDATED_CODE
        defaultCurrencyShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code not equals to DEFAULT_CODE
        defaultCurrencyShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the currencyList where code not equals to UPDATED_CODE
        defaultCurrencyShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCurrencyShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the currencyList where code equals to UPDATED_CODE
        defaultCurrencyShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code is not null
        defaultCurrencyShouldBeFound("code.specified=true");

        // Get all the currencyList where code is null
        defaultCurrencyShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code contains DEFAULT_CODE
        defaultCurrencyShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the currencyList where code contains UPDATED_CODE
        defaultCurrencyShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where code does not contain DEFAULT_CODE
        defaultCurrencyShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the currencyList where code does not contain UPDATED_CODE
        defaultCurrencyShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name equals to DEFAULT_NAME
        defaultCurrencyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the currencyList where name equals to UPDATED_NAME
        defaultCurrencyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name not equals to DEFAULT_NAME
        defaultCurrencyShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the currencyList where name not equals to UPDATED_NAME
        defaultCurrencyShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCurrencyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the currencyList where name equals to UPDATED_NAME
        defaultCurrencyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name is not null
        defaultCurrencyShouldBeFound("name.specified=true");

        // Get all the currencyList where name is null
        defaultCurrencyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name contains DEFAULT_NAME
        defaultCurrencyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the currencyList where name contains UPDATED_NAME
        defaultCurrencyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where name does not contain DEFAULT_NAME
        defaultCurrencyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the currencyList where name does not contain UPDATED_NAME
        defaultCurrencyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate equals to DEFAULT_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.equals=" + DEFAULT_CONVERSION_RATE);

        // Get all the currencyList where conversionRate equals to UPDATED_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.equals=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate not equals to DEFAULT_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.notEquals=" + DEFAULT_CONVERSION_RATE);

        // Get all the currencyList where conversionRate not equals to UPDATED_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.notEquals=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate in DEFAULT_CONVERSION_RATE or UPDATED_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.in=" + DEFAULT_CONVERSION_RATE + "," + UPDATED_CONVERSION_RATE);

        // Get all the currencyList where conversionRate equals to UPDATED_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.in=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate is not null
        defaultCurrencyShouldBeFound("conversionRate.specified=true");

        // Get all the currencyList where conversionRate is null
        defaultCurrencyShouldNotBeFound("conversionRate.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate is greater than or equal to DEFAULT_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.greaterThanOrEqual=" + DEFAULT_CONVERSION_RATE);

        // Get all the currencyList where conversionRate is greater than or equal to UPDATED_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.greaterThanOrEqual=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate is less than or equal to DEFAULT_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.lessThanOrEqual=" + DEFAULT_CONVERSION_RATE);

        // Get all the currencyList where conversionRate is less than or equal to SMALLER_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.lessThanOrEqual=" + SMALLER_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate is less than DEFAULT_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.lessThan=" + DEFAULT_CONVERSION_RATE);

        // Get all the currencyList where conversionRate is less than UPDATED_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.lessThan=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesByConversionRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where conversionRate is greater than DEFAULT_CONVERSION_RATE
        defaultCurrencyShouldNotBeFound("conversionRate.greaterThan=" + DEFAULT_CONVERSION_RATE);

        // Get all the currencyList where conversionRate is greater than SMALLER_CONVERSION_RATE
        defaultCurrencyShouldBeFound("conversionRate.greaterThan=" + SMALLER_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol equals to DEFAULT_SYMBOL
        defaultCurrencyShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the currencyList where symbol equals to UPDATED_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol not equals to DEFAULT_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.notEquals=" + DEFAULT_SYMBOL);

        // Get all the currencyList where symbol not equals to UPDATED_SYMBOL
        defaultCurrencyShouldBeFound("symbol.notEquals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultCurrencyShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the currencyList where symbol equals to UPDATED_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol is not null
        defaultCurrencyShouldBeFound("symbol.specified=true");

        // Get all the currencyList where symbol is null
        defaultCurrencyShouldNotBeFound("symbol.specified=false");
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol contains DEFAULT_SYMBOL
        defaultCurrencyShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the currencyList where symbol contains UPDATED_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol does not contain DEFAULT_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the currencyList where symbol does not contain UPDATED_SYMBOL
        defaultCurrencyShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllCurrenciesByTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        Transaction transaction = TransactionResourceIT.createEntity(em);
        em.persist(transaction);
        em.flush();
        currency.addTransaction(transaction);
        currencyRepository.saveAndFlush(currency);
        Long transactionId = transaction.getId();

        // Get all the currencyList where transaction equals to transactionId
        defaultCurrencyShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the currencyList where transaction equals to (transactionId + 1)
        defaultCurrencyShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    @Test
    @Transactional
    void getAllCurrenciesByScheduledTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        ScheduledTransaction scheduledTransaction = ScheduledTransactionResourceIT.createEntity(em);
        em.persist(scheduledTransaction);
        em.flush();
        currency.addScheduledTransaction(scheduledTransaction);
        currencyRepository.saveAndFlush(currency);
        Long scheduledTransactionId = scheduledTransaction.getId();

        // Get all the currencyList where scheduledTransaction equals to scheduledTransactionId
        defaultCurrencyShouldBeFound("scheduledTransactionId.equals=" + scheduledTransactionId);

        // Get all the currencyList where scheduledTransaction equals to (scheduledTransactionId + 1)
        defaultCurrencyShouldNotBeFound("scheduledTransactionId.equals=" + (scheduledTransactionId + 1));
    }

    @Test
    @Transactional
    void getAllCurrenciesByWalletIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        Wallet wallet = WalletResourceIT.createEntity(em);
        em.persist(wallet);
        em.flush();
        currency.addWallet(wallet);
        currencyRepository.saveAndFlush(currency);
        Long walletId = wallet.getId();

        // Get all the currencyList where wallet equals to walletId
        defaultCurrencyShouldBeFound("walletId.equals=" + walletId);

        // Get all the currencyList where wallet equals to (walletId + 1)
        defaultCurrencyShouldNotBeFound("walletId.equals=" + (walletId + 1));
    }

    @Test
    @Transactional
    void getAllCurrenciesByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        Invoice invoice = InvoiceResourceIT.createEntity(em);
        em.persist(invoice);
        em.flush();
        currency.addInvoice(invoice);
        currencyRepository.saveAndFlush(currency);
        Long invoiceId = invoice.getId();

        // Get all the currencyList where invoice equals to invoiceId
        defaultCurrencyShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the currencyList where invoice equals to (invoiceId + 1)
        defaultCurrencyShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCurrencyShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].conversionRate").value(hasItem(DEFAULT_CONVERSION_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)));

        // Check, that the count call also returns 1
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCurrencyShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).get();
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency.code(UPDATED_CODE).name(UPDATED_NAME).conversionRate(UPDATED_CONVERSION_RATE).symbol(UPDATED_SYMBOL);

        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCurrency.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getConversionRate()).isEqualTo(UPDATED_CONVERSION_RATE);
        assertThat(testCurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void putNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currency.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.name(UPDATED_NAME).symbol(UPDATED_SYMBOL);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getConversionRate()).isEqualTo(DEFAULT_CONVERSION_RATE);
        assertThat(testCurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.code(UPDATED_CODE).name(UPDATED_NAME).conversionRate(UPDATED_CONVERSION_RATE).symbol(UPDATED_SYMBOL);

        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            )
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getConversionRate()).isEqualTo(UPDATED_CONVERSION_RATE);
        assertThat(testCurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void patchNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();
        currency.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currency))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Delete the currency
        restCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, currency.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
