package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.service.criteria.TransactionCriteria;
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
 * Service for executing complex queries for {@link Transaction} entities in the database.
 * The main input is a {@link TransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Transaction} or a {@link Page} of {@link Transaction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionQueryService extends QueryService<Transaction> {

    private final Logger log = LoggerFactory.getLogger(TransactionQueryService.class);

    private final TransactionRepository transactionRepository;

    public TransactionQueryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Return a {@link List} of {@link Transaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Transaction> findByCriteria(TransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Transaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findByCriteria(TransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transaction> createSpecification(TransactionCriteria criteria) {
        Specification<Transaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transaction_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Transaction_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Transaction_.description));
            }
            if (criteria.getDateAdded() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAdded(), Transaction_.dateAdded));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Transaction_.amount));
            }
            if (criteria.getOriginalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOriginalAmount(), Transaction_.originalAmount));
            }
            if (criteria.getMovementType() != null) {
                specification = specification.and(buildSpecification(criteria.getMovementType(), Transaction_.movementType));
            }
            if (criteria.getScheduled() != null) {
                specification = specification.and(buildSpecification(criteria.getScheduled(), Transaction_.scheduled));
            }
            if (criteria.getAddToReports() != null) {
                specification = specification.and(buildSpecification(criteria.getAddToReports(), Transaction_.addToReports));
            }
            if (criteria.getIncomingTransaction() != null) {
                specification = specification.and(buildSpecification(criteria.getIncomingTransaction(), Transaction_.incomingTransaction));
            }
            if (criteria.getTransactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionType(), Transaction_.transactionType));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Transaction_.state));
            }
            if (criteria.getAttachmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAttachmentId(),
                            root -> root.join(Transaction_.attachment, JoinType.LEFT).get(Attachment_.id)
                        )
                    );
            }
            if (criteria.getWalletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWalletId(), root -> root.join(Transaction_.wallet, JoinType.LEFT).get(Wallet_.id))
                    );
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyId(),
                            root -> root.join(Transaction_.currency, JoinType.LEFT).get(Currency_.id)
                        )
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(Transaction_.category, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
            if (criteria.getSourceUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSourceUserId(),
                            root -> root.join(Transaction_.sourceUser, JoinType.LEFT).get(UserDetails_.id)
                        )
                    );
            }
            if (criteria.getRecievingUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRecievingUserId(),
                            root -> root.join(Transaction_.recievingUser, JoinType.LEFT).get(UserDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
