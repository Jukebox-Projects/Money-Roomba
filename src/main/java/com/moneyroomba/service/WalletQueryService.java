package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.criteria.WalletCriteria;
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
 * Service for executing complex queries for {@link Wallet} entities in the database.
 * The main input is a {@link WalletCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Wallet} or a {@link Page} of {@link Wallet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WalletQueryService extends QueryService<Wallet> {

    private final Logger log = LoggerFactory.getLogger(WalletQueryService.class);

    private final WalletRepository walletRepository;

    public WalletQueryService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Return a {@link List} of {@link Wallet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Wallet> findByCriteria(WalletCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Wallet> specification = createSpecification(criteria);
        return walletRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Wallet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Wallet> findByCriteria(WalletCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Wallet> specification = createSpecification(criteria);
        return walletRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WalletCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Wallet> specification = createSpecification(criteria);
        return walletRepository.count(specification);
    }

    /**
     * Function to convert {@link WalletCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Wallet> createSpecification(WalletCriteria criteria) {
        Specification<Wallet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Wallet_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Wallet_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Wallet_.description));
            }
            if (criteria.getInReports() != null) {
                specification = specification.and(buildSpecification(criteria.getInReports(), Wallet_.inReports));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Wallet_.isActive));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), Wallet_.balance));
            }
            if (criteria.getTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionId(),
                            root -> root.join(Wallet_.transactions, JoinType.LEFT).get(Transaction_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Wallet_.user, JoinType.LEFT).get(UserDetails_.id))
                    );
            }
            if (criteria.getIconId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getIconId(), root -> root.join(Wallet_.icon, JoinType.LEFT).get(Icon_.id))
                    );
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyId(), root -> root.join(Wallet_.currency, JoinType.LEFT).get(Currency_.id))
                    );
            }
        }
        return specification;
    }
}
