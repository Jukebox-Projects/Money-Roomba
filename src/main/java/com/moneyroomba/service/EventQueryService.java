package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.Event;
import com.moneyroomba.repository.EventRepository;
import com.moneyroomba.service.criteria.EventCriteria;
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
 * Service for executing complex queries for {@link Event} entities in the database.
 * The main input is a {@link EventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Event} or a {@link Page} of {@link Event} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventQueryService extends QueryService<Event> {

    private final Logger log = LoggerFactory.getLogger(EventQueryService.class);

    private final EventRepository eventRepository;

    public EventQueryService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Return a {@link List} of {@link Event} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Event> findByCriteria(EventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Event> specification = createSpecification(criteria);
        return eventRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Event} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Event> findByCriteria(EventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Event> specification = createSpecification(criteria);
        return eventRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Event> specification = createSpecification(criteria);
        return eventRepository.count(specification);
    }

    /**
     * Function to convert {@link EventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Event> createSpecification(EventCriteria criteria) {
        Specification<Event> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Event_.id));
            }
            if (criteria.getEventType() != null) {
                specification = specification.and(buildSpecification(criteria.getEventType(), Event_.eventType));
            }
            if (criteria.getDateAdded() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAdded(), Event_.dateAdded));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSourceId(), Event_.sourceId));
            }
            if (criteria.getSourceEntity() != null) {
                specification = specification.and(buildSpecification(criteria.getSourceEntity(), Event_.sourceEntity));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), Event_.userName));
            }
            if (criteria.getUserLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserLastName(), Event_.userLastName));
            }
            if (criteria.getNotificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificationId(),
                            root -> root.join(Event_.notification, JoinType.LEFT).get(Notification_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Event_.user, JoinType.LEFT).get(UserDetails_.id))
                    );
            }
        }
        return specification;
    }
}
