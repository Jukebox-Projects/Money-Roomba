package com.moneyroomba.service;

import com.moneyroomba.domain.Invoice;
import com.moneyroomba.repository.InvoiceRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Invoice}.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Partially update a invoice.
     *
     * @param invoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        log.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(
                existingInvoice -> {
                    if (invoice.getCompanyName() != null) {
                        existingInvoice.setCompanyName(invoice.getCompanyName());
                    }
                    if (invoice.getUserName() != null) {
                        existingInvoice.setUserName(invoice.getUserName());
                    }
                    if (invoice.getUserLastName() != null) {
                        existingInvoice.setUserLastName(invoice.getUserLastName());
                    }
                    if (invoice.getUserEmail() != null) {
                        existingInvoice.setUserEmail(invoice.getUserEmail());
                    }
                    if (invoice.getDateCreated() != null) {
                        existingInvoice.setDateCreated(invoice.getDateCreated());
                    }
                    if (invoice.getTotal() != null) {
                        existingInvoice.setTotal(invoice.getTotal());
                    }
                    if (invoice.getSubTotal() != null) {
                        existingInvoice.setSubTotal(invoice.getSubTotal());
                    }
                    if (invoice.getTax() != null) {
                        existingInvoice.setTax(invoice.getTax());
                    }
                    if (invoice.getPurchaseDescription() != null) {
                        existingInvoice.setPurchaseDescription(invoice.getPurchaseDescription());
                    }
                    if (invoice.getItemQuantity() != null) {
                        existingInvoice.setItemQuantity(invoice.getItemQuantity());
                    }
                    if (invoice.getItemPrice() != null) {
                        existingInvoice.setItemPrice(invoice.getItemPrice());
                    }

                    return existingInvoice;
                }
            )
            .map(invoiceRepository::save);
    }

    /**
     * Get all the invoices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll();
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
