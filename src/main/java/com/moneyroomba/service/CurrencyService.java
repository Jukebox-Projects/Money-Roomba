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

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Save a currency.
     *
     * @param currency the entity to save.
     * @return the persisted entity.
     */
    public Currency save(Currency currency) {
        log.debug("Request to save Currency : {}", currency);
        return currencyRepository.save(currency);
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
                    if (currency.getSymbol() != null) {
                        existingCurrency.setSymbol(currency.getSymbol());
                    }

                    return existingCurrency;
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
