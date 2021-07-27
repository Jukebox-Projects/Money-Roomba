package com.moneyroomba.service;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.repository.CategoryRepository;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.service.exception.Category.CategoryDepthException;
import com.moneyroomba.service.exception.Category.ParentCategoryIsSameCategory;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
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

    public CategoryService(CategoryRepository categoryRepository, UserService userService, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
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
                throw new ParentCategoryIsSameCategory("The parent category cannot be the same category");
            } else if (category.getParent().getParent() != null) {
                throw new CategoryDepthException("Child category cannot be a parent category");
            }
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
        return categoryRepository.findById(id);
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
            if (t.getCategory().equals(category)) {
                throw new BadRequestAlertException(
                    "Cannot delete category because there are transactions associated to it",
                    ENTITY_NAME,
                    "categorydeleteerror"
                );
            }
        }

        categoryRepository.deleteById(id);
    }
}
