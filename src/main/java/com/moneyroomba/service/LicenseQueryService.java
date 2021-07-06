package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.License;
import com.moneyroomba.repository.LicenseRepository;
import com.moneyroomba.service.criteria.LicenseCriteria;
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
 * Service for executing complex queries for {@link License} entities in the database.
 * The main input is a {@link LicenseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link License} or a {@link Page} of {@link License} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LicenseQueryService extends QueryService<License> {

    private final Logger log = LoggerFactory.getLogger(LicenseQueryService.class);

    private final LicenseRepository licenseRepository;

    public LicenseQueryService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    /**
     * Return a {@link List} of {@link License} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<License> findByCriteria(LicenseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<License> specification = createSpecification(criteria);
        return licenseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link License} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<License> findByCriteria(LicenseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<License> specification = createSpecification(criteria);
        return licenseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LicenseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<License> specification = createSpecification(criteria);
        return licenseRepository.count(specification);
    }

    /**
     * Function to convert {@link LicenseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<License> createSpecification(LicenseCriteria criteria) {
        Specification<License> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), License_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildSpecification(criteria.getCode(), License_.code));
            }
            if (criteria.getIsAssigned() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAssigned(), License_.isAssigned));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), License_.isActive));
            }
            if (criteria.getCreateMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getCreateMethod(), License_.createMethod));
            }
            if (criteria.getInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getInvoiceId(), root -> root.join(License_.invoice, JoinType.LEFT).get(Invoice_.id))
                    );
            }
        }
        return specification;
    }
}
