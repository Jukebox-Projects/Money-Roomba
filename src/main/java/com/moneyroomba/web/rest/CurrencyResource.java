package com.moneyroomba.web.rest;

import com.moneyroomba.domain.Currency;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.service.CurrencyQueryService;
import com.moneyroomba.service.CurrencyService;
import com.moneyroomba.service.criteria.CurrencyCriteria;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moneyroomba.domain.Currency}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;

    private final CurrencyQueryService currencyQueryService;

    public CurrencyResource(
        CurrencyService currencyService,
        CurrencyRepository currencyRepository,
        CurrencyQueryService currencyQueryService
    ) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
        this.currencyQueryService = currencyQueryService;
    }

    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     * @param currency the currency to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currency, or with status {@code 400 (Bad Request)} if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currencies")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currency);
        if (currency.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Currency result = currencyService.save(currency);
        return ResponseEntity
            .created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currencies/:id} : Updates an existing currency.
     *
     * @param id the id of the currency to save.
     * @param currency the currency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currency,
     * or with status {@code 400 (Bad Request)} if the currency is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currencies/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Currency> updateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Currency currency
    ) throws URISyntaxException {
        log.debug("REST request to update Currency : {}, {}", id, currency);
        if (currency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Currency result = currencyService.save(currency);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currency.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /currencies/:id} : Partial updates given fields of an existing currency, field will ignore if it is null
     *
     * @param id the id of the currency to save.
     * @param currency the currency to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currency,
     * or with status {@code 400 (Bad Request)} if the currency is not valid,
     * or with status {@code 404 (Not Found)} if the currency is not found,
     * or with status {@code 500 (Internal Server Error)} if the currency couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currencies/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Currency> partialUpdateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Currency currency
    ) throws URISyntaxException {
        log.debug("REST request to partial update Currency partially : {}, {}", id, currency);
        if (currency.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currency.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Currency> result = currencyService.partialUpdate(currency);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currency.getId().toString())
        );
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("/currencies")
    public ResponseEntity<List<Currency>> getAllCurrencies(CurrencyCriteria criteria) {
        log.debug("REST request to get Currencies by criteria: {}", criteria);
        List<Currency> entityList = currencyQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /currencies/count} : count all the currencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/currencies/count")
    public ResponseEntity<Long> countCurrencies(CurrencyCriteria criteria) {
        log.debug("REST request to count Currencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currency to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currency, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currencies/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        Optional<Currency> currency = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currency);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currency.
     *
     * @param id the id of the currency to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currencies/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
