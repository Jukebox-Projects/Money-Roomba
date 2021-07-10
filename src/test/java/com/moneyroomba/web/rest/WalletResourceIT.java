package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Icon;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.criteria.WalletCriteria;
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
 * Integration tests for the {@link WalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WalletResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IN_REPORTS = false;
    private static final Boolean UPDATED_IN_REPORTS = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;
    private static final Double SMALLER_BALANCE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletMockMvc;

    private Wallet wallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createEntity(EntityManager em) {
        Wallet wallet = new Wallet()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .inReports(DEFAULT_IN_REPORTS)
            .isActive(DEFAULT_IS_ACTIVE)
            .balance(DEFAULT_BALANCE);
        return wallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createUpdatedEntity(EntityManager em) {
        Wallet wallet = new Wallet()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .inReports(UPDATED_IN_REPORTS)
            .isActive(UPDATED_IS_ACTIVE)
            .balance(UPDATED_BALANCE);
        return wallet;
    }

    @BeforeEach
    public void initTest() {
        wallet = createEntity(em);
    }

    @Test
    @Transactional
    void createWallet() throws Exception {
        int databaseSizeBeforeCreate = walletRepository.findAll().size();
        // Create the Wallet
        restWalletMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isCreated());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate + 1);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWallet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWallet.getInReports()).isEqualTo(DEFAULT_IN_REPORTS);
        assertThat(testWallet.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testWallet.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void createWalletWithExistingId() throws Exception {
        // Create the Wallet with an existing ID
        wallet.setId(1L);

        int databaseSizeBeforeCreate = walletRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletRepository.findAll().size();
        // set the field null
        wallet.setName(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInReportsIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletRepository.findAll().size();
        // set the field null
        wallet.setInReports(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletRepository.findAll().size();
        // set the field null
        wallet.setIsActive(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletRepository.findAll().size();
        // set the field null
        wallet.setBalance(null);

        // Create the Wallet, which fails.

        restWalletMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWallets() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inReports").value(hasItem(DEFAULT_IN_REPORTS.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    void getWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get the wallet
        restWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, wallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wallet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.inReports").value(DEFAULT_IN_REPORTS.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    void getWalletsByIdFiltering() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        Long id = wallet.getId();

        defaultWalletShouldBeFound("id.equals=" + id);
        defaultWalletShouldNotBeFound("id.notEquals=" + id);

        defaultWalletShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWalletShouldNotBeFound("id.greaterThan=" + id);

        defaultWalletShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWalletShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWalletsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where name equals to DEFAULT_NAME
        defaultWalletShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the walletList where name equals to UPDATED_NAME
        defaultWalletShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWalletsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where name not equals to DEFAULT_NAME
        defaultWalletShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the walletList where name not equals to UPDATED_NAME
        defaultWalletShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWalletsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWalletShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the walletList where name equals to UPDATED_NAME
        defaultWalletShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWalletsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where name is not null
        defaultWalletShouldBeFound("name.specified=true");

        // Get all the walletList where name is null
        defaultWalletShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByNameContainsSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where name contains DEFAULT_NAME
        defaultWalletShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the walletList where name contains UPDATED_NAME
        defaultWalletShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWalletsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where name does not contain DEFAULT_NAME
        defaultWalletShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the walletList where name does not contain UPDATED_NAME
        defaultWalletShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWalletsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where description equals to DEFAULT_DESCRIPTION
        defaultWalletShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description equals to UPDATED_DESCRIPTION
        defaultWalletShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWalletsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where description not equals to DEFAULT_DESCRIPTION
        defaultWalletShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description not equals to UPDATED_DESCRIPTION
        defaultWalletShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWalletsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultWalletShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the walletList where description equals to UPDATED_DESCRIPTION
        defaultWalletShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWalletsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where description is not null
        defaultWalletShouldBeFound("description.specified=true");

        // Get all the walletList where description is null
        defaultWalletShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where description contains DEFAULT_DESCRIPTION
        defaultWalletShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description contains UPDATED_DESCRIPTION
        defaultWalletShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWalletsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where description does not contain DEFAULT_DESCRIPTION
        defaultWalletShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the walletList where description does not contain UPDATED_DESCRIPTION
        defaultWalletShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWalletsByInReportsIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where inReports equals to DEFAULT_IN_REPORTS
        defaultWalletShouldBeFound("inReports.equals=" + DEFAULT_IN_REPORTS);

        // Get all the walletList where inReports equals to UPDATED_IN_REPORTS
        defaultWalletShouldNotBeFound("inReports.equals=" + UPDATED_IN_REPORTS);
    }

    @Test
    @Transactional
    void getAllWalletsByInReportsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where inReports not equals to DEFAULT_IN_REPORTS
        defaultWalletShouldNotBeFound("inReports.notEquals=" + DEFAULT_IN_REPORTS);

        // Get all the walletList where inReports not equals to UPDATED_IN_REPORTS
        defaultWalletShouldBeFound("inReports.notEquals=" + UPDATED_IN_REPORTS);
    }

    @Test
    @Transactional
    void getAllWalletsByInReportsIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where inReports in DEFAULT_IN_REPORTS or UPDATED_IN_REPORTS
        defaultWalletShouldBeFound("inReports.in=" + DEFAULT_IN_REPORTS + "," + UPDATED_IN_REPORTS);

        // Get all the walletList where inReports equals to UPDATED_IN_REPORTS
        defaultWalletShouldNotBeFound("inReports.in=" + UPDATED_IN_REPORTS);
    }

    @Test
    @Transactional
    void getAllWalletsByInReportsIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where inReports is not null
        defaultWalletShouldBeFound("inReports.specified=true");

        // Get all the walletList where inReports is null
        defaultWalletShouldNotBeFound("inReports.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where isActive equals to DEFAULT_IS_ACTIVE
        defaultWalletShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the walletList where isActive equals to UPDATED_IS_ACTIVE
        defaultWalletShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWalletsByIsActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where isActive not equals to DEFAULT_IS_ACTIVE
        defaultWalletShouldNotBeFound("isActive.notEquals=" + DEFAULT_IS_ACTIVE);

        // Get all the walletList where isActive not equals to UPDATED_IS_ACTIVE
        defaultWalletShouldBeFound("isActive.notEquals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWalletsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultWalletShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the walletList where isActive equals to UPDATED_IS_ACTIVE
        defaultWalletShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWalletsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where isActive is not null
        defaultWalletShouldBeFound("isActive.specified=true");

        // Get all the walletList where isActive is null
        defaultWalletShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance equals to DEFAULT_BALANCE
        defaultWalletShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the walletList where balance equals to UPDATED_BALANCE
        defaultWalletShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance not equals to DEFAULT_BALANCE
        defaultWalletShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the walletList where balance not equals to UPDATED_BALANCE
        defaultWalletShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultWalletShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the walletList where balance equals to UPDATED_BALANCE
        defaultWalletShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is not null
        defaultWalletShouldBeFound("balance.specified=true");

        // Get all the walletList where balance is null
        defaultWalletShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is greater than or equal to DEFAULT_BALANCE
        defaultWalletShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the walletList where balance is greater than or equal to UPDATED_BALANCE
        defaultWalletShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is less than or equal to DEFAULT_BALANCE
        defaultWalletShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the walletList where balance is less than or equal to SMALLER_BALANCE
        defaultWalletShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is less than DEFAULT_BALANCE
        defaultWalletShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the walletList where balance is less than UPDATED_BALANCE
        defaultWalletShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        // Get all the walletList where balance is greater than DEFAULT_BALANCE
        defaultWalletShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the walletList where balance is greater than SMALLER_BALANCE
        defaultWalletShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllWalletsByTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);
        Transaction transaction = TransactionResourceIT.createEntity(em);
        em.persist(transaction);
        em.flush();
        wallet.addTransaction(transaction);
        walletRepository.saveAndFlush(wallet);
        Long transactionId = transaction.getId();

        // Get all the walletList where transaction equals to transactionId
        defaultWalletShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the walletList where transaction equals to (transactionId + 1)
        defaultWalletShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    @Test
    @Transactional
    void getAllWalletsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);
        UserDetails user = UserDetailsResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        wallet.setUser(user);
        walletRepository.saveAndFlush(wallet);
        Long userId = user.getId();

        // Get all the walletList where user equals to userId
        defaultWalletShouldBeFound("userId.equals=" + userId);

        // Get all the walletList where user equals to (userId + 1)
        defaultWalletShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllWalletsByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);
        Icon icon = IconResourceIT.createEntity(em);
        em.persist(icon);
        em.flush();
        wallet.setIcon(icon);
        walletRepository.saveAndFlush(wallet);
        Long iconId = icon.getId();

        // Get all the walletList where icon equals to iconId
        defaultWalletShouldBeFound("iconId.equals=" + iconId);

        // Get all the walletList where icon equals to (iconId + 1)
        defaultWalletShouldNotBeFound("iconId.equals=" + (iconId + 1));
    }

    @Test
    @Transactional
    void getAllWalletsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);
        Currency currency = CurrencyResourceIT.createEntity(em);
        em.persist(currency);
        em.flush();
        wallet.setCurrency(currency);
        walletRepository.saveAndFlush(wallet);
        Long currencyId = currency.getId();

        // Get all the walletList where currency equals to currencyId
        defaultWalletShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the walletList where currency equals to (currencyId + 1)
        defaultWalletShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWalletShouldBeFound(String filter) throws Exception {
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inReports").value(hasItem(DEFAULT_IN_REPORTS.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWalletShouldNotBeFound(String filter) throws Exception {
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWallet() throws Exception {
        // Get the wallet
        restWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet
        Wallet updatedWallet = walletRepository.findById(wallet.getId()).get();
        // Disconnect from session so that the updates on updatedWallet are not directly saved in db
        em.detach(updatedWallet);
        updatedWallet
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .inReports(UPDATED_IN_REPORTS)
            .isActive(UPDATED_IS_ACTIVE)
            .balance(UPDATED_BALANCE);

        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWallet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWallet.getInReports()).isEqualTo(UPDATED_IN_REPORTS);
        assertThat(testWallet.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWallet.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wallet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).inReports(UPDATED_IN_REPORTS).balance(UPDATED_BALANCE);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWallet.getInReports()).isEqualTo(UPDATED_IN_REPORTS);
        assertThat(testWallet.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testWallet.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .inReports(UPDATED_IN_REPORTS)
            .isActive(UPDATED_IS_ACTIVE)
            .balance(UPDATED_BALANCE);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWallet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWallet.getInReports()).isEqualTo(UPDATED_IN_REPORTS);
        assertThat(testWallet.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testWallet.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wallet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWallet() throws Exception {
        // Initialize the database
        walletRepository.saveAndFlush(wallet);

        int databaseSizeBeforeDelete = walletRepository.findAll().size();

        // Delete the wallet
        restWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, wallet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
