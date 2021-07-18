package com.moneyroomba.service;

import com.moneyroomba.domain.Currency;
import com.moneyroomba.repository.CurrencyRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Currency}.
 */
@Service
@Transactional
public class CurrencyService {

    private final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    private final UserService userService;

    public CurrencyService(CurrencyRepository currencyRepository, UserService userService) {
        this.currencyRepository = currencyRepository;
        this.userService = userService;
    }

    /**
     * Save a currency.
     *
     * @param currency the entity to save.
     * @return the persisted entity.
     */
    public Currency save(Currency currency, boolean postRequest) {
        log.debug("Request to save Currency : {}", currency);
        if (postRequest) {
            currency.setAdminCreated(true);
            currency.setIsActive(true);
            return currencyRepository.save(currency);
        } else {
            return partialUpdate(currency).get();
        }
    }

    /**
     * Partially update a currency.
     *
     * @param currency the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Currency> partialUpdate(Currency currency) {
        log.debug("Request to partially update Currency : {}", currency);

        return currencyRepository
            .findById(currency.getId())
            .map(
                existingCurrency -> {
                    if (currency.getCode() != null) {
                        existingCurrency.setCode(currency.getCode());
                    }
                    if (currency.getName() != null) {
                        existingCurrency.setName(currency.getName());
                    }
                    if (currency.getConversionRate() != null) {
                        existingCurrency.setConversionRate(currency.getConversionRate());
                    }

                    return existingCurrency;
                }
            )
            .map(currencyRepository::save);
    }

    /**
     * Partially update a category.
     *
     * @param id the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Currency> statusCategoryUpdate(Long id) {
        log.debug("Request to update Currency status : {}", id);

        return currencyRepository
            .findById(id)
            .map(
                existingCategory -> {
                    existingCategory.setIsActive(!existingCategory.getIsActive());
                    return existingCategory;
                }
            )
            .map(currencyRepository::save);
    }

    /**
     * Get all the currencies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        log.debug("Request to get all Currencies");
        return currencyRepository.findAll();
    }

    /**
     * Get one currency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Currency> findOne(Long id) {
        log.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id);
    }

    /**
     * Delete the currency by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Currency : {}", id);
        currencyRepository.deleteById(id);
    }
}
