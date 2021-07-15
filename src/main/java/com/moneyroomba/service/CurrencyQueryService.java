package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.Currency;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.service.criteria.CurrencyCriteria;
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
 * Service for executing complex queries for {@link Currency} entities in the database.
 * The main input is a {@link CurrencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Currency} or a {@link Page} of {@link Currency} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyQueryService extends QueryService<Currency> {

    private final Logger log = LoggerFactory.getLogger(CurrencyQueryService.class);

    private final CurrencyRepository currencyRepository;

    public CurrencyQueryService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Return a {@link List} of {@link Currency} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Currency> findByCriteria(CurrencyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Currency} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Currency> findByCriteria(CurrencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Currency> specification = createSpecification(criteria);
        return currencyRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Currency> createSpecification(CurrencyCriteria criteria) {
        Specification<Currency> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Currency_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Currency_.code));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Currency_.name));
            }
            if (criteria.getConversionRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConversionRate(), Currency_.conversionRate));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), Currency_.symbol));
            }
            if (criteria.getTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionId(),
                            root -> root.join(Currency_.transactions, JoinType.LEFT).get(Transaction_.id)
                        )
                    );
            }
            if (criteria.getScheduledTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getScheduledTransactionId(),
                            root -> root.join(Currency_.scheduledTransactions, JoinType.LEFT).get(ScheduledTransaction_.id)
                        )
                    );
            }
            if (criteria.getWalletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getWalletId(), root -> root.join(Currency_.wallets, JoinType.LEFT).get(Wallet_.id))
                    );
            }
            if (criteria.getInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getInvoiceId(), root -> root.join(Currency_.invoices, JoinType.LEFT).get(Invoice_.id))
                    );
            }
        }
        return specification;
    }
}
