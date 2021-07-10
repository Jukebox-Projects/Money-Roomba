package com.moneyroomba.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.moneyroomba.IntegrationTest;
import com.moneyroomba.domain.Event;
import com.moneyroomba.domain.Notification;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.EventRepository;
import com.moneyroomba.service.criteria.EventCriteria;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final EventType DEFAULT_EVENT_TYPE = EventType.TRANSCTION_RECEIVED;
    private static final EventType UPDATED_EVENT_TYPE = EventType.TRANSACTION_SENT;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ADDED = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_SOURCE_ID = 1L;
    private static final Long UPDATED_SOURCE_ID = 2L;
    private static final Long SMALLER_SOURCE_ID = 1L - 1L;

    private static final SourceEntity DEFAULT_SOURCE_ENTITY = SourceEntity.TRANSACTION;
    private static final SourceEntity UPDATED_SOURCE_ENTITY = SourceEntity.CONTACT;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .eventType(DEFAULT_EVENT_TYPE)
            .dateAdded(DEFAULT_DATE_ADDED)
            .sourceId(DEFAULT_SOURCE_ID)
            .sourceEntity(DEFAULT_SOURCE_ENTITY)
            .userName(DEFAULT_USER_NAME)
            .userLastName(DEFAULT_USER_LAST_NAME);
        return event;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .eventType(UPDATED_EVENT_TYPE)
            .dateAdded(UPDATED_DATE_ADDED)
            .sourceId(UPDATED_SOURCE_ID)
            .sourceEntity(UPDATED_SOURCE_ENTITY)
            .userName(UPDATED_USER_NAME)
            .userLastName(UPDATED_USER_LAST_NAME);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testEvent.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testEvent.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testEvent.getSourceEntity()).isEqualTo(DEFAULT_SOURCE_ENTITY);
        assertThat(testEvent.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testEvent.getUserLastName()).isEqualTo(DEFAULT_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);

        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventType(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setDateAdded(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setSourceId(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceEntityIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setSourceEntity(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setUserName(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setUserLastName(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceEntity").value(hasItem(DEFAULT_SOURCE_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userLastName").value(hasItem(DEFAULT_USER_LAST_NAME)));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.intValue()))
            .andExpect(jsonPath("$.sourceEntity").value(DEFAULT_SOURCE_ENTITY.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.userLastName").value(DEFAULT_USER_LAST_NAME));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventShouldBeFound("id.equals=" + id);
        defaultEventShouldNotBeFound("id.notEquals=" + id);

        defaultEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.greaterThan=" + id);

        defaultEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType equals to DEFAULT_EVENT_TYPE
        defaultEventShouldBeFound("eventType.equals=" + DEFAULT_EVENT_TYPE);

        // Get all the eventList where eventType equals to UPDATED_EVENT_TYPE
        defaultEventShouldNotBeFound("eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType not equals to DEFAULT_EVENT_TYPE
        defaultEventShouldNotBeFound("eventType.notEquals=" + DEFAULT_EVENT_TYPE);

        // Get all the eventList where eventType not equals to UPDATED_EVENT_TYPE
        defaultEventShouldBeFound("eventType.notEquals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType in DEFAULT_EVENT_TYPE or UPDATED_EVENT_TYPE
        defaultEventShouldBeFound("eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE);

        // Get all the eventList where eventType equals to UPDATED_EVENT_TYPE
        defaultEventShouldNotBeFound("eventType.in=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where eventType is not null
        defaultEventShouldBeFound("eventType.specified=true");

        // Get all the eventList where eventType is null
        defaultEventShouldNotBeFound("eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded equals to DEFAULT_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.equals=" + DEFAULT_DATE_ADDED);

        // Get all the eventList where dateAdded equals to UPDATED_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.equals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded not equals to DEFAULT_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.notEquals=" + DEFAULT_DATE_ADDED);

        // Get all the eventList where dateAdded not equals to UPDATED_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.notEquals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded in DEFAULT_DATE_ADDED or UPDATED_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.in=" + DEFAULT_DATE_ADDED + "," + UPDATED_DATE_ADDED);

        // Get all the eventList where dateAdded equals to UPDATED_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.in=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded is not null
        defaultEventShouldBeFound("dateAdded.specified=true");

        // Get all the eventList where dateAdded is null
        defaultEventShouldNotBeFound("dateAdded.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded is greater than or equal to DEFAULT_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.greaterThanOrEqual=" + DEFAULT_DATE_ADDED);

        // Get all the eventList where dateAdded is greater than or equal to UPDATED_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.greaterThanOrEqual=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded is less than or equal to DEFAULT_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.lessThanOrEqual=" + DEFAULT_DATE_ADDED);

        // Get all the eventList where dateAdded is less than or equal to SMALLER_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.lessThanOrEqual=" + SMALLER_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded is less than DEFAULT_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.lessThan=" + DEFAULT_DATE_ADDED);

        // Get all the eventList where dateAdded is less than UPDATED_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.lessThan=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsByDateAddedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where dateAdded is greater than DEFAULT_DATE_ADDED
        defaultEventShouldNotBeFound("dateAdded.greaterThan=" + DEFAULT_DATE_ADDED);

        // Get all the eventList where dateAdded is greater than SMALLER_DATE_ADDED
        defaultEventShouldBeFound("dateAdded.greaterThan=" + SMALLER_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId equals to DEFAULT_SOURCE_ID
        defaultEventShouldBeFound("sourceId.equals=" + DEFAULT_SOURCE_ID);

        // Get all the eventList where sourceId equals to UPDATED_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId not equals to DEFAULT_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.notEquals=" + DEFAULT_SOURCE_ID);

        // Get all the eventList where sourceId not equals to UPDATED_SOURCE_ID
        defaultEventShouldBeFound("sourceId.notEquals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultEventShouldBeFound("sourceId.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the eventList where sourceId equals to UPDATED_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId is not null
        defaultEventShouldBeFound("sourceId.specified=true");

        // Get all the eventList where sourceId is null
        defaultEventShouldNotBeFound("sourceId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId is greater than or equal to DEFAULT_SOURCE_ID
        defaultEventShouldBeFound("sourceId.greaterThanOrEqual=" + DEFAULT_SOURCE_ID);

        // Get all the eventList where sourceId is greater than or equal to UPDATED_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.greaterThanOrEqual=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId is less than or equal to DEFAULT_SOURCE_ID
        defaultEventShouldBeFound("sourceId.lessThanOrEqual=" + DEFAULT_SOURCE_ID);

        // Get all the eventList where sourceId is less than or equal to SMALLER_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.lessThanOrEqual=" + SMALLER_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId is less than DEFAULT_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.lessThan=" + DEFAULT_SOURCE_ID);

        // Get all the eventList where sourceId is less than UPDATED_SOURCE_ID
        defaultEventShouldBeFound("sourceId.lessThan=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceId is greater than DEFAULT_SOURCE_ID
        defaultEventShouldNotBeFound("sourceId.greaterThan=" + DEFAULT_SOURCE_ID);

        // Get all the eventList where sourceId is greater than SMALLER_SOURCE_ID
        defaultEventShouldBeFound("sourceId.greaterThan=" + SMALLER_SOURCE_ID);
    }

    @Test
    @Transactional
    void getAllEventsBySourceEntityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceEntity equals to DEFAULT_SOURCE_ENTITY
        defaultEventShouldBeFound("sourceEntity.equals=" + DEFAULT_SOURCE_ENTITY);

        // Get all the eventList where sourceEntity equals to UPDATED_SOURCE_ENTITY
        defaultEventShouldNotBeFound("sourceEntity.equals=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllEventsBySourceEntityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceEntity not equals to DEFAULT_SOURCE_ENTITY
        defaultEventShouldNotBeFound("sourceEntity.notEquals=" + DEFAULT_SOURCE_ENTITY);

        // Get all the eventList where sourceEntity not equals to UPDATED_SOURCE_ENTITY
        defaultEventShouldBeFound("sourceEntity.notEquals=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllEventsBySourceEntityIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceEntity in DEFAULT_SOURCE_ENTITY or UPDATED_SOURCE_ENTITY
        defaultEventShouldBeFound("sourceEntity.in=" + DEFAULT_SOURCE_ENTITY + "," + UPDATED_SOURCE_ENTITY);

        // Get all the eventList where sourceEntity equals to UPDATED_SOURCE_ENTITY
        defaultEventShouldNotBeFound("sourceEntity.in=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllEventsBySourceEntityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where sourceEntity is not null
        defaultEventShouldBeFound("sourceEntity.specified=true");

        // Get all the eventList where sourceEntity is null
        defaultEventShouldNotBeFound("sourceEntity.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userName equals to DEFAULT_USER_NAME
        defaultEventShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the eventList where userName equals to UPDATED_USER_NAME
        defaultEventShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userName not equals to DEFAULT_USER_NAME
        defaultEventShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the eventList where userName not equals to UPDATED_USER_NAME
        defaultEventShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultEventShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the eventList where userName equals to UPDATED_USER_NAME
        defaultEventShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userName is not null
        defaultEventShouldBeFound("userName.specified=true");

        // Get all the eventList where userName is null
        defaultEventShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUserNameContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userName contains DEFAULT_USER_NAME
        defaultEventShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the eventList where userName contains UPDATED_USER_NAME
        defaultEventShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userName does not contain DEFAULT_USER_NAME
        defaultEventShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the eventList where userName does not contain UPDATED_USER_NAME
        defaultEventShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userLastName equals to DEFAULT_USER_LAST_NAME
        defaultEventShouldBeFound("userLastName.equals=" + DEFAULT_USER_LAST_NAME);

        // Get all the eventList where userLastName equals to UPDATED_USER_LAST_NAME
        defaultEventShouldNotBeFound("userLastName.equals=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userLastName not equals to DEFAULT_USER_LAST_NAME
        defaultEventShouldNotBeFound("userLastName.notEquals=" + DEFAULT_USER_LAST_NAME);

        // Get all the eventList where userLastName not equals to UPDATED_USER_LAST_NAME
        defaultEventShouldBeFound("userLastName.notEquals=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userLastName in DEFAULT_USER_LAST_NAME or UPDATED_USER_LAST_NAME
        defaultEventShouldBeFound("userLastName.in=" + DEFAULT_USER_LAST_NAME + "," + UPDATED_USER_LAST_NAME);

        // Get all the eventList where userLastName equals to UPDATED_USER_LAST_NAME
        defaultEventShouldNotBeFound("userLastName.in=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userLastName is not null
        defaultEventShouldBeFound("userLastName.specified=true");

        // Get all the eventList where userLastName is null
        defaultEventShouldNotBeFound("userLastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUserLastNameContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userLastName contains DEFAULT_USER_LAST_NAME
        defaultEventShouldBeFound("userLastName.contains=" + DEFAULT_USER_LAST_NAME);

        // Get all the eventList where userLastName contains UPDATED_USER_LAST_NAME
        defaultEventShouldNotBeFound("userLastName.contains=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByUserLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where userLastName does not contain DEFAULT_USER_LAST_NAME
        defaultEventShouldNotBeFound("userLastName.doesNotContain=" + DEFAULT_USER_LAST_NAME);

        // Get all the eventList where userLastName does not contain UPDATED_USER_LAST_NAME
        defaultEventShouldBeFound("userLastName.doesNotContain=" + UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByNotificationIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        Notification notification = NotificationResourceIT.createEntity(em);
        em.persist(notification);
        em.flush();
        event.setNotification(notification);
        eventRepository.saveAndFlush(event);
        Long notificationId = notification.getId();

        // Get all the eventList where notification equals to notificationId
        defaultEventShouldBeFound("notificationId.equals=" + notificationId);

        // Get all the eventList where notification equals to (notificationId + 1)
        defaultEventShouldNotBeFound("notificationId.equals=" + (notificationId + 1));
    }

    @Test
    @Transactional
    void getAllEventsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        UserDetails user = UserDetailsResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        event.setUser(user);
        eventRepository.saveAndFlush(event);
        Long userId = user.getId();

        // Get all the eventList where user equals to userId
        defaultEventShouldBeFound("userId.equals=" + userId);

        // Get all the eventList where user equals to (userId + 1)
        defaultEventShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceEntity").value(hasItem(DEFAULT_SOURCE_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].userLastName").value(hasItem(DEFAULT_USER_LAST_NAME)));

        // Check, that the count call also returns 1
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .eventType(UPDATED_EVENT_TYPE)
            .dateAdded(UPDATED_DATE_ADDED)
            .sourceId(UPDATED_SOURCE_ID)
            .sourceEntity(UPDATED_SOURCE_ENTITY)
            .userName(UPDATED_USER_NAME)
            .userLastName(UPDATED_USER_LAST_NAME);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testEvent.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testEvent.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testEvent.getSourceEntity()).isEqualTo(UPDATED_SOURCE_ENTITY);
        assertThat(testEvent.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testEvent.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, event.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent.dateAdded(UPDATED_DATE_ADDED);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);
        assertThat(testEvent.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testEvent.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testEvent.getSourceEntity()).isEqualTo(DEFAULT_SOURCE_ENTITY);
        assertThat(testEvent.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testEvent.getUserLastName()).isEqualTo(DEFAULT_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .eventType(UPDATED_EVENT_TYPE)
            .dateAdded(UPDATED_DATE_ADDED)
            .sourceId(UPDATED_SOURCE_ID)
            .sourceEntity(UPDATED_SOURCE_ENTITY)
            .userName(UPDATED_USER_NAME)
            .userLastName(UPDATED_USER_LAST_NAME);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);
        assertThat(testEvent.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testEvent.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testEvent.getSourceEntity()).isEqualTo(UPDATED_SOURCE_ENTITY);
        assertThat(testEvent.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testEvent.getUserLastName()).isEqualTo(UPDATED_USER_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, event.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();
        event.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(event))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
