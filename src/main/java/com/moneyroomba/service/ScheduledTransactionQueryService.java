package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.repository.ScheduledTransactionRepository;
import com.moneyroomba.service.criteria.ScheduledTransactionCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ScheduledTransaction} entities in the database.
 * The main input is a {@link ScheduledTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ScheduledTransaction} or a {@link Page} of {@link ScheduledTransaction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduledTransactionQueryService extends QueryService<ScheduledTransaction> {

    private final Logger log = LoggerFactory.getLogger(ScheduledTransactionQueryService.class);

    private final ScheduledTransactionRepository scheduledTransactionRepository;

    public ScheduledTransactionQueryService(ScheduledTransactionRepository scheduledTransactionRepository) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
    }

    /**
     * Return a {@link List} of {@link ScheduledTransaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ScheduledTransaction> findByCriteria(ScheduledTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ScheduledTransaction> specification = createSpecification(criteria);
        return scheduledTransactionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ScheduledTransaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduledTransaction> findByCriteria(ScheduledTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScheduledTransaction> specification = createSpecification(criteria);
        return scheduledTransactionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScheduledTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ScheduledTransaction> specification = createSpecification(criteria);
        return scheduledTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link ScheduledTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ScheduledTransaction> createSpecification(ScheduledTransactionCriteria criteria) {
        Specification<ScheduledTransaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ScheduledTransaction_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ScheduledTransaction_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ScheduledTransaction_.description));
            }
            if (criteria.getOriginalAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOriginalAmount(), ScheduledTransaction_.originalAmount));
            }
            if (criteria.getMovementType() != null) {
                specification = specification.and(buildSpecification(criteria.getMovementType(), ScheduledTransaction_.movementType));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), ScheduledTransaction_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), ScheduledTransaction_.endDate));
            }
            if (criteria.getAddToReports() != null) {
                specification = specification.and(buildSpecification(criteria.getAddToReports(), ScheduledTransaction_.addToReports));
            }
            if (criteria.getRecurringType() != null) {
                specification = specification.and(buildSpecification(criteria.getRecurringType(), ScheduledTransaction_.recurringType));
            }
            if (criteria.getSeparationCount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSeparationCount(), ScheduledTransaction_.separationCount));
            }
            if (criteria.getMaxNumberOfOcurrences() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getMaxNumberOfOcurrences(), ScheduledTransaction_.maxNumberOfOcurrences)
                    );
            }
            if (criteria.getDayOfWeek() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDayOfWeek(), ScheduledTransaction_.dayOfWeek));
            }
            if (criteria.getWeekOfMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeekOfMonth(), ScheduledTransaction_.weekOfMonth));
            }
            if (criteria.getDayOfMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDayOfMonth(), ScheduledTransaction_.dayOfMonth));
            }
            if (criteria.getMonthOfYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonthOfYear(), ScheduledTransaction_.monthOfYear));
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyId(),
                            root -> root.join(ScheduledTransaction_.currency, JoinType.LEFT).get(Currency_.id)
                        )
                    );
            }
            if (criteria.getSourceUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSourceUserId(),
                            root -> root.join(ScheduledTransaction_.sourceUser, JoinType.LEFT).get(UserDetails_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(ScheduledTransaction_.category, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
