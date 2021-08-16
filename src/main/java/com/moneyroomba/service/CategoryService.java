package com.moneyroomba.service;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Event;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.CategoryRepository;
import com.moneyroomba.repository.EventRepository;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.exception.Category.CategoryDepthException;
import com.moneyroomba.service.exception.Category.ParentCategoryIsSameCategory;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Category}.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private static final String ENTITY_NAME = "category";

    private final TransactionRepository transactionRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    public CategoryService(
        CategoryRepository categoryRepository,
        UserService userService,
        TransactionRepository transactionRepository,
        EventRepository eventRepository,
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * Save a category.
     *
     * @param category the entity to save.
     * @return the persisted entity.
     */
    public Category save(Category category) {
        log.debug("Request to save Category : {}", category);
        // Checks category depth. It can only goes 2 levels deep
        if (category.getParent() != null) {
            if (category.getParent().getId().equals(category.getId())) {
                throw new BadRequestAlertException("A category cannot be its own parent", ENTITY_NAME, "categoryOwnParent");
            } else if (category.getParent().getParent() != null) {
                throw new BadRequestAlertException("Child category cannot be a parent category", ENTITY_NAME, "childCategoryAsParent");
            } else if (categoryRepository.findOneByParentId(category.getId()).isPresent()) {
                throw new BadRequestAlertException(
                    "Parent category cannot be a child category at the same time.",
                    ENTITY_NAME,
                    "categoryIsParent"
                );
            }
        }
        if (category.getId() != null) {
            createEvent(EventType.UPDATE);
            Optional<User> user = userRepository.findOneByLogin(
                SecurityUtils
                    .getCurrentUserLogin()
                    .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
            );
            Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
            Category existingCategory = categoryRepository.getOne(category.getId());
            if (
                (userService.currentUserIsAdmin() && existingCategory.getUserCreated()) ||
                (
                    !userService.currentUserIsAdmin() &&
                    (
                        (existingCategory.getUserCreated() && !userDetails.get().equals(existingCategory.getUser())) ||
                        !existingCategory.getUserCreated()
                    )
                )
            ) {
                throw new BadRequestAlertException(
                    "You cannot access or modify this wallet's information",
                    ENTITY_NAME,
                    "categorynoaccess"
                );
            }
        } else {
            createEvent(EventType.CREATE);
        }
        return categoryRepository.save(category);
    }

    /**
     * Partially update a category.
     *
     * @param category the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Category> partialUpdate(Category category) {
        log.debug("Request to partially update Category : {}", category);
        createEvent(EventType.UPDATE);
        return categoryRepository
            .findById(category.getId())
            .map(
                existingCategory -> {
                    if (category.getName() != null) {
                        existingCategory.setName(category.getName());
                    }
                    if (category.getIsActive() != null) {
                        existingCategory.setIsActive(category.getIsActive());
                    }
                    if (category.getUserCreated() != null) {
                        existingCategory.setUserCreated(category.getUserCreated());
                    }
                    if (category.getIcon() != null) {
                        existingCategory.setIcon(category.getIcon());
                    }

                    return existingCategory;
                }
            )
            .map(categoryRepository::save);
    }

    /**
     * Partially update a category.
     *
     * @param id the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Category> statusCategoryUpdate(Long id) {
        log.debug("Request to update Category status : {}", id);
        createEvent(EventType.UPDATE);
        return categoryRepository
            .findById(id)
            .map(
                //(!category.userCreated && adminUser) || (category.userCreated && !adminUser)
                existingCategory -> {
                    final boolean expectedStatus = !existingCategory.getIsActive();
                    if ((long) existingCategory.getCategories().size() > 0) {
                        existingCategory.getCategories().forEach(category -> childStatusChange(category, expectedStatus));
                    }
                    existingCategory.setIsActive(expectedStatus);
                    return existingCategory;
                }
            )
            .map(categoryRepository::save);
    }

    private void childStatusChange(Category category, boolean status) {
        log.debug("Request to update child Category status : {} {}", category.getId(), category);
        category.setIsActive(status);
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll();
    }

    /**
     * Get one category by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Category> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        Optional<Category> category = categoryRepository.findById(id);
        if (
            (userService.currentUserIsAdmin() && category.get().getUserCreated()) ||
            (!userService.currentUserIsAdmin() && (category.get().getUserCreated() && !userDetails.get().equals(category.get().getUser())))
        ) {
            throw new BadRequestAlertException("You cannot access or modify this wallet's information", ENTITY_NAME, "categorynoaccess");
        }
        return category;
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        List<Transaction> entityList = transactionRepository.findAll();

        for (Transaction t : entityList) {
            if (t.getCategory() != null && t.getCategory().equals(category.get())) {
                throw new BadRequestAlertException(
                    "Cannot delete category because there are transactions associated to it",
                    ENTITY_NAME,
                    "categorydeleteerror"
                );
            }
        }
        createEvent(EventType.DELETE);
        categoryRepository.deleteById(id);
    }

    /**
     * Create event.
     *
     * @param eventType of the entity.
     */
    public void createEvent(EventType eventType) {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
        );

        Event event = new Event();
        event.setEventType(eventType);
        event.setDateAdded(LocalDate.now());
        event.setSourceId(user.get().getId());
        event.setSourceEntity(SourceEntity.CATEGORY);
        event.setUserName(user.get().getFirstName());
        event.setUserLastName(user.get().getLastName());
        System.out.println(event);
        eventRepository.save(event);
    }
}
