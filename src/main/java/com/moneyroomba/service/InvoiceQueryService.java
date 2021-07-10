package com.moneyroomba.service;

import com.moneyroomba.domain.*; // for static metamodels
import com.moneyroomba.domain.Invoice;
import com.moneyroomba.repository.InvoiceRepository;
import com.moneyroomba.service.criteria.InvoiceCriteria;
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
 * Service for executing complex queries for {@link Invoice} entities in the database.
 * The main input is a {@link InvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Invoice} or a {@link Page} of {@link Invoice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private final Logger log = LoggerFactory.getLogger(InvoiceQueryService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Return a {@link List} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Invoice> findByCriteria(InvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Invoice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Invoice> specification = createSpecification(criteria);
        return invoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specification<Invoice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Invoice_.id));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Invoice_.companyName));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), Invoice_.userName));
            }
            if (criteria.getUserLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserLastName(), Invoice_.userLastName));
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserEmail(), Invoice_.userEmail));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Invoice_.dateCreated));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Invoice_.total));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), Invoice_.subTotal));
            }
            if (criteria.getTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTax(), Invoice_.tax));
            }
            if (criteria.getPurchaseDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPurchaseDescription(), Invoice_.purchaseDescription));
            }
            if (criteria.getItemQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemQuantity(), Invoice_.itemQuantity));
            }
            if (criteria.getItemPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getItemPrice(), Invoice_.itemPrice));
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyId(), root -> root.join(Invoice_.currency, JoinType.LEFT).get(Currency_.id))
                    );
            }
        }
        return specification;
    }
}
