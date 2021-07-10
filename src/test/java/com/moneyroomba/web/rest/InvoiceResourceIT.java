package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.Invoice;
import com.moneyroomba.repository.InvoiceRepository;
import com.moneyroomba.service.criteria.InvoiceCriteria;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATED = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_TOTAL = 0D;
    private static final Double UPDATED_TOTAL = 1D;
    private static final Double SMALLER_TOTAL = 0D - 1D;

    private static final Double DEFAULT_SUB_TOTAL = 0D;
    private static final Double UPDATED_SUB_TOTAL = 1D;
    private static final Double SMALLER_SUB_TOTAL = 0D - 1D;

    private static final Double DEFAULT_TAX = 0D;
    private static final Double UPDATED_TAX = 1D;
    private static final Double SMALLER_TAX = 0D - 1D;

    private static final String DEFAULT_PURCHASE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PURCHASE_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ITEM_QUANTITY = 0;
    private static final Integer UPDATED_ITEM_QUANTITY = 1;
    private static final Integer SMALLER_ITEM_QUANTITY = 0 - 1;

    private static final Double DEFAULT_ITEM_PRICE = 0D;
    private static final Double UPDATED_ITEM_PRICE = 1D;
    private static final Double SMALLER_ITEM_PRICE = 0D - 1D;

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .companyName(DEFAULT_COMPANY_NAME)
            .userName(DEFAULT_USER_NAME)
            .userLastName(DEFAULT_USER_LAST_NAME)
            .userEmail(DEFAULT_USER_EMAIL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .total(DEFAULT_TOTAL)
            .subTotal(DEFAULT_SUB_TOTAL)
            .tax(DEFAULT_TAX)
            .purchaseDescription(DEFAULT_PURCHASE_DESCRIPTION)
            .itemQuantity(DEFAULT_ITEM_QUANTITY)
            .itemPrice(DEFAULT_ITEM_PRICE);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .companyName(UPDATED_COMPANY_NAME)
            .userName(UPDATED_USER_NAME)
            .userLastName(UPDATED_USER_LAST_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .total(UPDATED_TOTAL)
            .subTotal(UPDATED_SUB_TOTAL)
            .tax(UPDATED_TAX)
            .purchaseDescription(UPDATED_PURCHASE_DESCRIPTION)
            .itemQuantity(UPDATED_ITEM_QUANTITY)
            .itemPrice(UPDATED_ITEM_PRICE);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testInvoice.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testInvoice.getUserLastName()).isEqualTo(DEFAULT_USER_LAST_NAME);
        assertThat(testInvoice.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testInvoice.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testInvoice.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testInvoice.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testInvoice.getPurchaseDescription()).isEqualTo(DEFAULT_PURCHASE_DESCRIPTION);
        assertThat(testInvoice.getItemQuantity()).isEqualTo(DEFAULT_ITEM_QUANTITY);
        assertThat(testInvoice.getItemPrice()).isEqualTo(DEFAULT_ITEM_PRICE);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setCompanyName(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setUserName(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setUserLastName(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setUserEmail(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setDateCreated(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTotal(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setSubTotal(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkItemQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setItemQuantity(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkItemPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setItemPrice(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userLastName").value(hasItem(DEFAULT_USER_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].purchaseDescription").value(hasItem(DEFAULT_PURCHASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemQuantity").value(hasItem(DEFAULT_ITEM_QUANTITY)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userLastName").value(DEFAULT_USER_LAST_NAME))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.purchaseDescription").value(DEFAULT_PURCHASE_DESCRIPTION))
            .andExpect(jsonPath("$.itemQuantity").value(DEFAULT_ITEM_QUANTITY))
            .andExpect(jsonPath("$.itemPrice").value(DEFAULT_ITEM_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        Long id = invoice.getId();

        defaultInvoiceShouldBeFound("id.equals=" + id);
        defaultInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoicesByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where companyName equals to DEFAULT_COMPANY_NAME
        defaultInvoiceShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the invoiceList where companyName equals to UPDATED_COMPANY_NAME
        defaultInvoiceShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByCompanyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where companyName not equals to DEFAULT_COMPANY_NAME
        defaultInvoiceShouldNotBeFound("companyName.notEquals=" + DEFAULT_COMPANY_NAME);

        // Get all the invoiceList where companyName not equals to UPDATED_COMPANY_NAME
        defaultInvoiceShouldBeFound("companyName.notEquals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultInvoiceShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the invoiceList where companyName equals to UPDATED_COMPANY_NAME
        defaultInvoiceShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where companyName is not null
        defaultInvoiceShouldBeFound("companyName.specified=true");

        // Get all the invoiceList where companyName is null
        defaultInvoiceShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where companyName contains DEFAULT_COMPANY_NAME
        defaultInvoiceShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the invoiceList where companyName contains UPDATED_COMPANY_NAME
        defaultInvoiceShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultInvoiceShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the invoiceList where companyName does not contain UPDATED_COMPANY_NAME
        defaultInvoiceShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userName equals to DEFAULT_USER_NAME
        defaultInvoiceShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the invoiceList where userName equals to UPDATED_USER_NAME
        defaultInvoiceShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userName not equals to DEFAULT_USER_NAME
        defaultInvoiceShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the invoiceList where userName not equals to UPDATED_USER_NAME
        defaultInvoiceShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultInvoiceShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the invoiceList where userName equals to UPDATED_USER_NAME
        defaultInvoiceShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userName is not null
        defaultInvoiceShouldBeFound("userName.specified=true");

        // Get all the invoiceList where userName is null
        defaultInvoiceShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByUserNameContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userName contains DEFAULT_USER_NAME
        defaultInvoiceShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the invoiceList where userName contains UPDATED_USER_NAME
        defaultInvoiceShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userName does not contain DEFAULT_USER_NAME
        defaultInvoiceShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the invoiceList where userName does not contain UPDATED_USER_NAME
        defaultInvoiceShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userLastName equals to DEFAULT_USER_LAST_NAME
        defaultInvoiceShouldBeFound("userLastName.equals=" + DEFAULT_USER_LAST_NAME);

        // Get all the invoiceList where userLastName equals to UPDATED_USER_LAST_NAME
        defaultInvoiceShouldNotBeFound("userLastName.equals=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userLastName not equals to DEFAULT_USER_LAST_NAME
        defaultInvoiceShouldNotBeFound("userLastName.notEquals=" + DEFAULT_USER_LAST_NAME);

        // Get all the invoiceList where userLastName not equals to UPDATED_USER_LAST_NAME
        defaultInvoiceShouldBeFound("userLastName.notEquals=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userLastName in DEFAULT_USER_LAST_NAME or UPDATED_USER_LAST_NAME
        defaultInvoiceShouldBeFound("userLastName.in=" + DEFAULT_USER_LAST_NAME + "," + UPDATED_USER_LAST_NAME);

        // Get all the invoiceList where userLastName equals to UPDATED_USER_LAST_NAME
        defaultInvoiceShouldNotBeFound("userLastName.in=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userLastName is not null
        defaultInvoiceShouldBeFound("userLastName.specified=true");

        // Get all the invoiceList where userLastName is null
        defaultInvoiceShouldNotBeFound("userLastName.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByUserLastNameContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userLastName contains DEFAULT_USER_LAST_NAME
        defaultInvoiceShouldBeFound("userLastName.contains=" + DEFAULT_USER_LAST_NAME);

        // Get all the invoiceList where userLastName contains UPDATED_USER_LAST_NAME
        defaultInvoiceShouldNotBeFound("userLastName.contains=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userLastName does not contain DEFAULT_USER_LAST_NAME
        defaultInvoiceShouldNotBeFound("userLastName.doesNotContain=" + DEFAULT_USER_LAST_NAME);

        // Get all the invoiceList where userLastName does not contain UPDATED_USER_LAST_NAME
        defaultInvoiceShouldBeFound("userLastName.doesNotContain=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userEmail equals to DEFAULT_USER_EMAIL
        defaultInvoiceShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the invoiceList where userEmail equals to UPDATED_USER_EMAIL
        defaultInvoiceShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userEmail not equals to DEFAULT_USER_EMAIL
        defaultInvoiceShouldNotBeFound("userEmail.notEquals=" + DEFAULT_USER_EMAIL);

        // Get all the invoiceList where userEmail not equals to UPDATED_USER_EMAIL
        defaultInvoiceShouldBeFound("userEmail.notEquals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultInvoiceShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the invoiceList where userEmail equals to UPDATED_USER_EMAIL
        defaultInvoiceShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userEmail is not null
        defaultInvoiceShouldBeFound("userEmail.specified=true");

        // Get all the invoiceList where userEmail is null
        defaultInvoiceShouldNotBeFound("userEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userEmail contains DEFAULT_USER_EMAIL
        defaultInvoiceShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the invoiceList where userEmail contains UPDATED_USER_EMAIL
        defaultInvoiceShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllInvoicesByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultInvoiceShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the invoiceList where userEmail does not contain UPDATED_USER_EMAIL
        defaultInvoiceShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the invoiceList where dateCreated equals to UPDATED_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the invoiceList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the invoiceList where dateCreated equals to UPDATED_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated is not null
        defaultInvoiceShouldBeFound("dateCreated.specified=true");

        // Get all the invoiceList where dateCreated is null
        defaultInvoiceShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated is greater than or equal to DEFAULT_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.greaterThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the invoiceList where dateCreated is greater than or equal to UPDATED_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.greaterThanOrEqual=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated is less than or equal to DEFAULT_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.lessThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the invoiceList where dateCreated is less than or equal to SMALLER_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.lessThanOrEqual=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated is less than DEFAULT_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.lessThan=" + DEFAULT_DATE_CREATED);

        // Get all the invoiceList where dateCreated is less than UPDATED_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.lessThan=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByDateCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where dateCreated is greater than DEFAULT_DATE_CREATED
        defaultInvoiceShouldNotBeFound("dateCreated.greaterThan=" + DEFAULT_DATE_CREATED);

        // Get all the invoiceList where dateCreated is greater than SMALLER_DATE_CREATED
        defaultInvoiceShouldBeFound("dateCreated.greaterThan=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total equals to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total not equals to DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total not equals to UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is not null
        defaultInvoiceShouldBeFound("total.specified=true");

        // Get all the invoiceList where total is null
        defaultInvoiceShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is greater than or equal to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is greater than or equal to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is less than or equal to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is less than or equal to SMALLER_TOTAL
        defaultInvoiceShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is less than DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is less than UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is greater than DEFAULT_TOTAL
        defaultInvoiceShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total is greater than SMALLER_TOTAL
        defaultInvoiceShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal equals to UPDATED_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal not equals to DEFAULT_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.notEquals=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal not equals to UPDATED_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.notEquals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the invoiceList where subTotal equals to UPDATED_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal is not null
        defaultInvoiceShouldBeFound("subTotal.specified=true");

        // Get all the invoiceList where subTotal is null
        defaultInvoiceShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal is greater than or equal to DEFAULT_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.greaterThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal is greater than or equal to UPDATED_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.greaterThanOrEqual=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal is less than or equal to DEFAULT_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.lessThanOrEqual=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal is less than or equal to SMALLER_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.lessThanOrEqual=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal is less than DEFAULT_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.lessThan=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal is less than UPDATED_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.lessThan=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesBySubTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal is greater than DEFAULT_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.greaterThan=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal is greater than SMALLER_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.greaterThan=" + SMALLER_SUB_TOTAL);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax equals to DEFAULT_TAX
        defaultInvoiceShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the invoiceList where tax equals to UPDATED_TAX
        defaultInvoiceShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax not equals to DEFAULT_TAX
        defaultInvoiceShouldNotBeFound("tax.notEquals=" + DEFAULT_TAX);

        // Get all the invoiceList where tax not equals to UPDATED_TAX
        defaultInvoiceShouldBeFound("tax.notEquals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultInvoiceShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the invoiceList where tax equals to UPDATED_TAX
        defaultInvoiceShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax is not null
        defaultInvoiceShouldBeFound("tax.specified=true");

        // Get all the invoiceList where tax is null
        defaultInvoiceShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax is greater than or equal to DEFAULT_TAX
        defaultInvoiceShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the invoiceList where tax is greater than or equal to UPDATED_TAX
        defaultInvoiceShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax is less than or equal to DEFAULT_TAX
        defaultInvoiceShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the invoiceList where tax is less than or equal to SMALLER_TAX
        defaultInvoiceShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax is less than DEFAULT_TAX
        defaultInvoiceShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the invoiceList where tax is less than UPDATED_TAX
        defaultInvoiceShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where tax is greater than DEFAULT_TAX
        defaultInvoiceShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the invoiceList where tax is greater than SMALLER_TAX
        defaultInvoiceShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllInvoicesByPurchaseDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where purchaseDescription equals to DEFAULT_PURCHASE_DESCRIPTION
        defaultInvoiceShouldBeFound("purchaseDescription.equals=" + DEFAULT_PURCHASE_DESCRIPTION);

        // Get all the invoiceList where purchaseDescription equals to UPDATED_PURCHASE_DESCRIPTION
        defaultInvoiceShouldNotBeFound("purchaseDescription.equals=" + UPDATED_PURCHASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoicesByPurchaseDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where purchaseDescription not equals to DEFAULT_PURCHASE_DESCRIPTION
        defaultInvoiceShouldNotBeFound("purchaseDescription.notEquals=" + DEFAULT_PURCHASE_DESCRIPTION);

        // Get all the invoiceList where purchaseDescription not equals to UPDATED_PURCHASE_DESCRIPTION
        defaultInvoiceShouldBeFound("purchaseDescription.notEquals=" + UPDATED_PURCHASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoicesByPurchaseDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where purchaseDescription in DEFAULT_PURCHASE_DESCRIPTION or UPDATED_PURCHASE_DESCRIPTION
        defaultInvoiceShouldBeFound("purchaseDescription.in=" + DEFAULT_PURCHASE_DESCRIPTION + "," + UPDATED_PURCHASE_DESCRIPTION);

        // Get all the invoiceList where purchaseDescription equals to UPDATED_PURCHASE_DESCRIPTION
        defaultInvoiceShouldNotBeFound("purchaseDescription.in=" + UPDATED_PURCHASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoicesByPurchaseDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where purchaseDescription is not null
        defaultInvoiceShouldBeFound("purchaseDescription.specified=true");

        // Get all the invoiceList where purchaseDescription is null
        defaultInvoiceShouldNotBeFound("purchaseDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByPurchaseDescriptionContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where purchaseDescription contains DEFAULT_PURCHASE_DESCRIPTION
        defaultInvoiceShouldBeFound("purchaseDescription.contains=" + DEFAULT_PURCHASE_DESCRIPTION);

        // Get all the invoiceList where purchaseDescription contains UPDATED_PURCHASE_DESCRIPTION
        defaultInvoiceShouldNotBeFound("purchaseDescription.contains=" + UPDATED_PURCHASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoicesByPurchaseDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where purchaseDescription does not contain DEFAULT_PURCHASE_DESCRIPTION
        defaultInvoiceShouldNotBeFound("purchaseDescription.doesNotContain=" + DEFAULT_PURCHASE_DESCRIPTION);

        // Get all the invoiceList where purchaseDescription does not contain UPDATED_PURCHASE_DESCRIPTION
        defaultInvoiceShouldBeFound("purchaseDescription.doesNotContain=" + UPDATED_PURCHASE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity equals to DEFAULT_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.equals=" + DEFAULT_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity equals to UPDATED_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.equals=" + UPDATED_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity not equals to DEFAULT_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.notEquals=" + DEFAULT_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity not equals to UPDATED_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.notEquals=" + UPDATED_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity in DEFAULT_ITEM_QUANTITY or UPDATED_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.in=" + DEFAULT_ITEM_QUANTITY + "," + UPDATED_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity equals to UPDATED_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.in=" + UPDATED_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity is not null
        defaultInvoiceShouldBeFound("itemQuantity.specified=true");

        // Get all the invoiceList where itemQuantity is null
        defaultInvoiceShouldNotBeFound("itemQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity is greater than or equal to DEFAULT_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.greaterThanOrEqual=" + DEFAULT_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity is greater than or equal to UPDATED_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.greaterThanOrEqual=" + UPDATED_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity is less than or equal to DEFAULT_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.lessThanOrEqual=" + DEFAULT_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity is less than or equal to SMALLER_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.lessThanOrEqual=" + SMALLER_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity is less than DEFAULT_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.lessThan=" + DEFAULT_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity is less than UPDATED_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.lessThan=" + UPDATED_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemQuantity is greater than DEFAULT_ITEM_QUANTITY
        defaultInvoiceShouldNotBeFound("itemQuantity.greaterThan=" + DEFAULT_ITEM_QUANTITY);

        // Get all the invoiceList where itemQuantity is greater than SMALLER_ITEM_QUANTITY
        defaultInvoiceShouldBeFound("itemQuantity.greaterThan=" + SMALLER_ITEM_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice equals to DEFAULT_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.equals=" + DEFAULT_ITEM_PRICE);

        // Get all the invoiceList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.equals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice not equals to DEFAULT_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.notEquals=" + DEFAULT_ITEM_PRICE);

        // Get all the invoiceList where itemPrice not equals to UPDATED_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.notEquals=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice in DEFAULT_ITEM_PRICE or UPDATED_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.in=" + DEFAULT_ITEM_PRICE + "," + UPDATED_ITEM_PRICE);

        // Get all the invoiceList where itemPrice equals to UPDATED_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.in=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice is not null
        defaultInvoiceShouldBeFound("itemPrice.specified=true");

        // Get all the invoiceList where itemPrice is null
        defaultInvoiceShouldNotBeFound("itemPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice is greater than or equal to DEFAULT_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.greaterThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the invoiceList where itemPrice is greater than or equal to UPDATED_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.greaterThanOrEqual=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice is less than or equal to DEFAULT_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.lessThanOrEqual=" + DEFAULT_ITEM_PRICE);

        // Get all the invoiceList where itemPrice is less than or equal to SMALLER_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.lessThanOrEqual=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice is less than DEFAULT_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.lessThan=" + DEFAULT_ITEM_PRICE);

        // Get all the invoiceList where itemPrice is less than UPDATED_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.lessThan=" + UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByItemPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where itemPrice is greater than DEFAULT_ITEM_PRICE
        defaultInvoiceShouldNotBeFound("itemPrice.greaterThan=" + DEFAULT_ITEM_PRICE);

        // Get all the invoiceList where itemPrice is greater than SMALLER_ITEM_PRICE
        defaultInvoiceShouldBeFound("itemPrice.greaterThan=" + SMALLER_ITEM_PRICE);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Currency currency = CurrencyResourceIT.createEntity(em);
        em.persist(currency);
        em.flush();
        invoice.setCurrency(currency);
        invoiceRepository.saveAndFlush(invoice);
        Long currencyId = currency.getId();

        // Get all the invoiceList where currency equals to currencyId
        defaultInvoiceShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the invoiceList where currency equals to (currencyId + 1)
        defaultInvoiceShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userLastName").value(hasItem(DEFAULT_USER_LAST_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].purchaseDescription").value(hasItem(DEFAULT_PURCHASE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].itemQuantity").value(hasItem(DEFAULT_ITEM_QUANTITY)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .companyName(UPDATED_COMPANY_NAME)
            .userName(UPDATED_USER_NAME)
            .userLastName(UPDATED_USER_LAST_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .total(UPDATED_TOTAL)
            .subTotal(UPDATED_SUB_TOTAL)
            .tax(UPDATED_TAX)
            .purchaseDescription(UPDATED_PURCHASE_DESCRIPTION)
            .itemQuantity(UPDATED_ITEM_QUANTITY)
            .itemPrice(UPDATED_ITEM_PRICE);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoice.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testInvoice.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testInvoice.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
        assertThat(testInvoice.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testInvoice.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testInvoice.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testInvoice.getPurchaseDescription()).isEqualTo(UPDATED_PURCHASE_DESCRIPTION);
        assertThat(testInvoice.getItemQuantity()).isEqualTo(UPDATED_ITEM_QUANTITY);
        assertThat(testInvoice.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoice.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .userLastName(UPDATED_USER_LAST_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .purchaseDescription(UPDATED_PURCHASE_DESCRIPTION)
            .itemQuantity(UPDATED_ITEM_QUANTITY)
            .itemPrice(UPDATED_ITEM_PRICE);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testInvoice.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testInvoice.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
        assertThat(testInvoice.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testInvoice.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testInvoice.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testInvoice.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testInvoice.getPurchaseDescription()).isEqualTo(UPDATED_PURCHASE_DESCRIPTION);
        assertThat(testInvoice.getItemQuantity()).isEqualTo(UPDATED_ITEM_QUANTITY);
        assertThat(testInvoice.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .companyName(UPDATED_COMPANY_NAME)
            .userName(UPDATED_USER_NAME)
            .userLastName(UPDATED_USER_LAST_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .dateCreated(UPDATED_DATE_CREATED)
            .total(UPDATED_TOTAL)
            .subTotal(UPDATED_SUB_TOTAL)
            .tax(UPDATED_TAX)
            .purchaseDescription(UPDATED_PURCHASE_DESCRIPTION)
            .itemQuantity(UPDATED_ITEM_QUANTITY)
            .itemPrice(UPDATED_ITEM_PRICE);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testInvoice.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testInvoice.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
        assertThat(testInvoice.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testInvoice.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testInvoice.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testInvoice.getPurchaseDescription()).isEqualTo(UPDATED_PURCHASE_DESCRIPTION);
        assertThat(testInvoice.getItemQuantity()).isEqualTo(UPDATED_ITEM_QUANTITY);
        assertThat(testInvoice.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoice.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
