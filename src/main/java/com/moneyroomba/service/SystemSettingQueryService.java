package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.SystemSetting;
import com.moneyroomba.repository.SystemSettingRepository;
import com.moneyroomba.service.criteria.SystemSettingCriteria;
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
 * Service for executing complex queries for {@link SystemSetting} entities in the database.
 * The main input is a {@link SystemSettingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemSetting} or a {@link Page} of {@link SystemSetting} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemSettingQueryService extends QueryService<SystemSetting> {

    private final Logger log = LoggerFactory.getLogger(SystemSettingQueryService.class);

    private final SystemSettingRepository systemSettingRepository;

    public SystemSettingQueryService(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    /**
     * Return a {@link List} of {@link SystemSetting} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemSetting> findByCriteria(SystemSettingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SystemSetting> specification = createSpecification(criteria);
        return systemSettingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SystemSetting} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemSetting> findByCriteria(SystemSettingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemSetting> specification = createSpecification(criteria);
        return systemSettingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemSettingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SystemSetting> specification = createSpecification(criteria);
        return systemSettingRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemSettingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemSetting> createSpecification(SystemSettingCriteria criteria) {
        Specification<SystemSetting> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SystemSetting_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SystemSetting_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), SystemSetting_.value));
            }
        }
        return specification;
    }
}
