package com.moneyroomba.service;

import com.moneyroomba.domain.*;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.*;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private static class TransactionServiceException extends RuntimeException {

        private TransactionServiceException(String message) {
            super(message);
        }
    }

    private static final String ENTITY_NAME = "transaction";

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final WalletRepository walletRepository;

    private final CurrencyRepository currencyRepository;

    public TransactionService(
        TransactionRepository transactionRepository,
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        WalletRepository walletRepository,
        CurrencyRepository currencyRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.walletRepository = walletRepository;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Transaction save(Transaction transaction) {
        double currentBalance;
        Wallet wallet = transaction.getWallet();
        log.debug("Request to save Transaction : {}", transaction);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Optional<User> user = userRepository.findOneByLogin(
                SecurityUtils
                    .getCurrentUserLogin()
                    .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
            );
            if (transaction.getId() != null) {
                Optional<Transaction> existingTransaction = transactionRepository.findById(transaction.getId());
                wallet = existingTransaction.get().getWallet();
                if (existingTransaction.get().getMovementType().equals(MovementType.EXPENSE)) {
                    wallet.setBalance(wallet.getBalance() + existingTransaction.get().getAmount());
                } else {
                    wallet.setBalance(wallet.getBalance() - existingTransaction.get().getAmount());
                }
            }
            Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
            transaction.setSourceUser(userDetails.get());
            transaction.setTransactionType(TransactionType.MANUAL);
            transaction.setIncomingTransaction(false);
            transaction.setScheduled(false);
            //logica de parseo de monedas....
            if (transaction.getCurrency().equals(wallet.getCurrency())) {
                transaction.setAmount(transaction.getOriginalAmount());
            } else {
                double transactionInDollars = transaction.getOriginalAmount() / transaction.getCurrency().getConversionRate();
                transaction.setAmount(transactionInDollars * wallet.getCurrency().getConversionRate());
            }
            currentBalance = transaction.getWallet().getBalance();
            if (transaction.getMovementType().equals(MovementType.EXPENSE)) {
                if (wallet.getBalance() > 0 && wallet.getBalance() >= transaction.getAmount()) {
                    currentBalance = wallet.getBalance();
                    currentBalance = currentBalance - transaction.getAmount();
                    wallet.setBalance(currentBalance);
                    walletRepository.save(wallet);
                } else {
                    //throw insufficient funds exception
                    throw new BadRequestAlertException(
                        "You cannot register this transaction because of insufficient balance.",
                        ENTITY_NAME,
                        "insufficientfunds"
                    );
                }
            } else {
                if (transaction.getAmount() > 0) {
                    currentBalance = wallet.getBalance();
                    currentBalance = currentBalance + transaction.getAmount();
                    wallet.setBalance(currentBalance);
                    walletRepository.save(wallet);
                } else {
                    //throw cannot register negative transaction exception.
                    throw new BadRequestAlertException(
                        "You cannot register a transaction with an income lower than 0.",
                        ENTITY_NAME,
                        "negativeincome"
                    );
                }
            }
            return transactionRepository.save(transaction);
        } else {
            throw new BadRequestAlertException("Los administradores no pueden crear transacciones", ENTITY_NAME, "nopermission");
        }
    }

    /**
     * Partially update a transaction.
     *
     * @param transaction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        log.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(
                existingTransaction -> {
                    if (transaction.getName() != null) {
                        existingTransaction.setName(transaction.getName());
                    }
                    if (transaction.getDescription() != null) {
                        existingTransaction.setDescription(transaction.getDescription());
                    }
                    if (transaction.getDateAdded() != null) {
                        existingTransaction.setDateAdded(transaction.getDateAdded());
                    }
                    if (transaction.getAmount() != null) {
                        existingTransaction.setAmount(transaction.getAmount());
                    }
                    if (transaction.getOriginalAmount() != null) {
                        existingTransaction.setOriginalAmount(transaction.getOriginalAmount());
                        Wallet wallet = transaction.getWallet();
                        if (transaction.getId() != null) {
                            //remover monto anterior
                            if (existingTransaction.getMovementType().equals(MovementType.EXPENSE)) {
                                wallet.setBalance(wallet.getBalance() + existingTransaction.getAmount());
                            } else {
                                wallet.setBalance(wallet.getBalance() - existingTransaction.getAmount());
                            }
                            if (transaction.getCurrency().equals(wallet.getCurrency())) {
                                existingTransaction.setAmount(transaction.getOriginalAmount());
                            } else {
                                double transactionInDollars =
                                    transaction.getOriginalAmount() / transaction.getCurrency().getConversionRate();
                                existingTransaction.setAmount(transactionInDollars * wallet.getCurrency().getConversionRate());
                            }
                            double currentBalance;
                            if (transaction.getMovementType().equals(MovementType.EXPENSE)) {
                                if (wallet.getBalance() > 0 && wallet.getBalance() >= existingTransaction.getAmount()) {
                                    currentBalance = wallet.getBalance();
                                    currentBalance = currentBalance - existingTransaction.getAmount();
                                    wallet.setBalance(currentBalance);
                                    walletRepository.save(wallet);
                                } else {
                                    //throw insufficient funds exception
                                    throw new BadRequestAlertException(
                                        "You cannot register this transaction because of insufficient balance.",
                                        ENTITY_NAME,
                                        "insufficientfunds"
                                    );
                                }
                            } else {
                                if (transaction.getAmount() > 0) {
                                    currentBalance = wallet.getBalance();
                                    currentBalance = currentBalance + existingTransaction.getAmount();
                                    wallet.setBalance(currentBalance);
                                    walletRepository.save(wallet);
                                } else {
                                    //throw cannot register negative transaction exception.
                                    throw new BadRequestAlertException(
                                        "You cannot register a transaction with an income lower than 0.",
                                        ENTITY_NAME,
                                        "negativeincome"
                                    );
                                }
                            }
                            walletRepository.save(wallet);
                        }
                    }
                    if (transaction.getMovementType() != null) {
                        //logica de deshacer transaccion en balance y efectuar nueva transaccion
                        //PENDIENTE
                        existingTransaction.setMovementType(transaction.getMovementType());
                    }
                    if (transaction.getScheduled() != null) {
                        existingTransaction.setScheduled(transaction.getScheduled());
                    }
                    if (transaction.getAddToReports() != null) {
                        existingTransaction.setAddToReports(transaction.getAddToReports());
                    }
                    if (transaction.getIncomingTransaction() != null) {
                        existingTransaction.setIncomingTransaction(transaction.getIncomingTransaction());
                    }
                    if (transaction.getTransactionType() != null) {
                        existingTransaction.setTransactionType(transaction.getTransactionType());
                    }

                    return existingTransaction;
                }
            )
            .map(transactionRepository::save);
    }

    /**
     * Get all the transactions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll();
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        Wallet wallet = existingTransaction.get().getWallet();
        if (existingTransaction.get().getMovementType().equals(MovementType.EXPENSE)) {
            wallet.setBalance(wallet.getBalance() + existingTransaction.get().getAmount());
        } else {
            wallet.setBalance(wallet.getBalance() - existingTransaction.get().getAmount());
        }
        walletRepository.save(wallet);
        transactionRepository.deleteById(id);
    }
}
