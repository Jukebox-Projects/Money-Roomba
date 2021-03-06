package com.moneyroomba.web.rest;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.TransactionQueryService;
import com.moneyroomba.service.TransactionService;
import com.moneyroomba.service.UserService;
import com.moneyroomba.service.criteria.TransactionCriteria;
import com.moneyroomba.service.dto.TransactionDTO;
import com.moneyroomba.service.exception.NoSuchElementFoundException;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moneyroomba.domain.Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;

    private final TransactionRepository transactionRepository;

    private final TransactionQueryService transactionQueryService;

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    public TransactionResource(
        TransactionService transactionService,
        TransactionRepository transactionRepository,
        TransactionQueryService transactionQueryService,
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository
    ) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.transactionQueryService = transactionQueryService;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    public Transaction getTransactionFromDTO(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        UserDetails receivingUser = null;
        if (dto.getRecievingUser() != null) {
            receivingUser = userDetailsRepository.findOneByPhone(dto.getRecievingUser().getPhone()).get();
        }

        transaction.setId(dto.getId());
        transaction.setName(dto.getName());
        transaction.setDescription(dto.getDescription());
        transaction.setDateAdded(dto.getDateAdded());
        transaction.setAmount(dto.getAmount());
        transaction.setOriginalAmount(dto.getOriginalAmount());
        transaction.setMovementType(dto.getMovementType());
        transaction.setScheduled(dto.getScheduled());
        transaction.setAddToReports(dto.getAddToReports());
        transaction.setIncomingTransaction(dto.getIncomingTransaction());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setState(dto.getState());
        transaction.setAttachment(dto.getAttachment());
        transaction.setWallet(dto.getWallet());
        transaction.setCurrency(dto.getCurrency());
        transaction.setCategory(dto.getCategory());
        transaction.setSourceUser(dto.getSourceUser());
        transaction.setRecievingUser(receivingUser);
        return transaction;
    }

    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaction, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transactionDTO);
        Transaction transaction = getTransactionFromDTO(transactionDTO);
        Transaction result;
        if (transaction.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            if (transaction.getRecievingUser() != null) {
                result = transactionService.saveOutgoingTransaction(transaction);
            } else {
                result = transactionService.create(transaction);
            }

            return ResponseEntity
                .created(new URI("/api/transactions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else {
            throw new BadRequestAlertException("Admins cannot register transactions.", ENTITY_NAME, "admincantregister");
        }
    }

    /**
     * {@code PUT  /transactions/:id} : Updates an existing transaction.
     *
     * @param id the id of the transaction to save.
     * @param transaction the transaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaction,
     * or with status {@code 400 (Bad Request)} if the transaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions/{id}")
    public ResponseEntity<Transaction> updateTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}, {}", id, transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transaction result = transactionService.update(transaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transactions/:id} : Partial updates given fields of an existing transaction, field will ignore if it is null
     *
     * @param id the id of the transaction to save.
     * @param transaction the transaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaction,
     * or with status {@code 400 (Bad Request)} if the transaction is not valid,
     * or with status {@code 404 (Not Found)} if the transaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the transaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transactions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Transaction> partialUpdateTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaction partially : {}, {}", id, transaction);
        if (transaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Transaction> result = transactionService.partialUpdate(transaction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaction.getId().toString())
        );
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(TransactionCriteria criteria) {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new NoSuchElementFoundException("No Login found"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        List<Transaction> entityList = transactionRepository.findAllOrderedByDateAdded();
        List<Transaction> res = new ArrayList<Transaction>();
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            List<Transaction> resAll = transactionRepository.findAllOrderedByDateAdded();
            log.debug("Request to get all transactions for Admin");
            return ResponseEntity.ok().body(resAll);
        } else {
            if (userDetails.isPresent()) {
                for (Transaction transaction : entityList) {
                    if (transaction.getSourceUser() == null) {} else if (transaction.getSourceUser().equals(userDetails.get())) {
                        res.add(transaction);
                    }
                }
            }
        }
        return ResponseEntity.ok().body(res);
    }

    /**
     * {@code GET  /transactions/count} : count all the transactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transactions/count")
    public ResponseEntity<Long> countTransactions(TransactionCriteria criteria) {
        log.debug("REST request to count Transactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<Transaction> transaction = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transaction);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transaction.
     *
     * @param id the id of the transaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/wallet/{id}")
    public ResponseEntity<List<Transaction>> getTransactionsByWallet(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        List<Transaction> entityList = transactionService.findAllByWallet(id);
        return ResponseEntity.ok().body(entityList);
    }
}
