package com.moneyroomba.service;

import com.moneyroomba.domain.*;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.*;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.dto.factura.CodigoTipoMoneda;
import com.moneyroomba.service.dto.factura.LineaDetalle;
import com.moneyroomba.service.dto.factura.ResumenFactura;
import com.moneyroomba.service.dto.factura.TiqueteElectronico;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.ZoneId;
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
        Optional<Wallet> targetWallet = walletRepository.findById(wallet.getId());
        log.debug("Request to save Transaction : {}", transaction);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Optional<User> user = userRepository.findOneByLogin(
                SecurityUtils
                    .getCurrentUserLogin()
                    .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
            );
            if (transaction.getId() != null) {
                Optional<Transaction> existingTransaction = transactionRepository.findById(transaction.getId());
                Optional<Wallet> registeredWallet = walletRepository.findById(existingTransaction.get().getWallet().getId());
                if (
                    existingTransaction.get().getState() != null &&
                    existingTransaction.get().getState() != TransactionState.NA &&
                    existingTransaction.get().getState().equals(TransactionState.PENDING_APPROVAL) &&
                    (transaction.getState().equals(TransactionState.ACCEPTED) || transaction.getState().equals(TransactionState.DENIED))
                ) {
                    if (transaction.getState().equals(TransactionState.DENIED)) {
                        transactionRepository.delete(transaction);
                        return null;
                    }
                    transaction.setState(TransactionState.NA);
                } else {
                    if (registeredWallet.get().equals(targetWallet)) {
                        //si no cambia el wallet
                        if (existingTransaction.get().getMovementType().equals(MovementType.EXPENSE)) {
                            targetWallet.get().setBalance(wallet.getBalance() + existingTransaction.get().getAmount());
                        } else {
                            targetWallet.get().setBalance(wallet.getBalance() - existingTransaction.get().getAmount());
                        }
                    } else {
                        //si cambia el wallet
                        if (existingTransaction.get().getMovementType().equals(MovementType.EXPENSE)) {
                            registeredWallet.get().setBalance(registeredWallet.get().getBalance() + existingTransaction.get().getAmount());
                        } else {
                            registeredWallet.get().setBalance(registeredWallet.get().getBalance() - existingTransaction.get().getAmount());
                        }
                        walletRepository.save(registeredWallet.get());
                    }
                }

                if (transaction.getTransactionType() == TransactionType.API || transaction.getTransactionType() == TransactionType.EMAIL) {
                    Optional<Wallet> walletSelected = walletRepository.findById(transaction.getWallet().getId());
                    if (transaction.getOriginalAmount() != existingTransaction.get().getOriginalAmount()) {
                        if (transaction.getCurrency().equals(walletSelected.get().getCurrency())) {
                            transaction.setAmount(transaction.getOriginalAmount());
                        } else {
                            Optional<Currency> colon = this.currencyRepository.findOneByCode("CRC");
                            double tipoCambioColonDolar = 0.0;
                            if (colon.isPresent()) {
                                tipoCambioColonDolar = colon.get().getConversionRate();
                            } else {
                                throw new BadRequestAlertException("Currency 'CRC' was not found", ENTITY_NAME, "");
                            }

                            double rate = existingTransaction.get().getAmount() / existingTransaction.get().getOriginalAmount();
                            double transactionAmount = transaction.getOriginalAmount();
                            transaction.setAmount(transactionAmount * rate);
                        }
                    }

                    if (transaction.getMovementType().equals(MovementType.EXPENSE)) {
                        if (targetWallet.get().getBalance() > 0 && targetWallet.get().getBalance() >= transaction.getAmount()) {
                            currentBalance = targetWallet.get().getBalance();
                            currentBalance = currentBalance - transaction.getAmount();
                            targetWallet.get().setBalance(currentBalance);
                            walletRepository.save(targetWallet.get());
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
                            currentBalance = targetWallet.get().getBalance();
                            currentBalance = currentBalance + transaction.getAmount();
                            targetWallet.get().setBalance(currentBalance);
                            walletRepository.save(targetWallet.get());
                        } else {
                            //throw cannot register negative transaction exception.
                            throw new BadRequestAlertException(
                                "You cannot register a transaction with an income lower than 0.",
                                ENTITY_NAME,
                                "negativeincome"
                            );
                        }
                    }
                    transaction.setState(TransactionState.NA);
                    return transactionRepository.save(transaction);
                }
            }
            Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
            transaction.setSourceUser(userDetails.get());
            transaction.setIncomingTransaction(false);
            transaction.setScheduled(false);
            transaction.setTransactionType(TransactionType.MANUAL);
            if (transaction.getCurrency().equals(targetWallet.get().getCurrency())) {
                transaction.setAmount(transaction.getOriginalAmount());
            } else {
                double transactionInDollars = transaction.getOriginalAmount() / transaction.getCurrency().getConversionRate();
                transaction.setAmount(transactionInDollars * targetWallet.get().getCurrency().getConversionRate());
            }

            if (transaction.getMovementType().equals(MovementType.EXPENSE)) {
                if (targetWallet.get().getBalance() > 0 && targetWallet.get().getBalance() >= transaction.getAmount()) {
                    currentBalance = targetWallet.get().getBalance();
                    currentBalance = currentBalance - transaction.getAmount();
                    targetWallet.get().setBalance(currentBalance);
                    walletRepository.save(targetWallet.get());
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
                    currentBalance = targetWallet.get().getBalance();
                    currentBalance = currentBalance + transaction.getAmount();
                    targetWallet.get().setBalance(currentBalance);
                    walletRepository.save(targetWallet.get());
                } else {
                    //throw cannot register negative transaction exception.
                    throw new BadRequestAlertException(
                        "You cannot register a transaction with an income lower than 0.",
                        ENTITY_NAME,
                        "negativeincome"
                    );
                }
            }
            transaction.setState(TransactionState.NA);
            return transactionRepository.save(transaction);
        } else {
            throw new BadRequestAlertException("Los administradores no pueden crear transacciones", ENTITY_NAME, "nopermission");
        }
    }

    @Transactional
    public Transaction saveOutgoingTransaction(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Optional<User> user = userRepository.findOneByLogin(
                SecurityUtils
                    .getCurrentUserLogin()
                    .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
            );
            Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
            transaction.setSourceUser(userDetails.get());
            UserDetails receivingUser = transaction.getRecievingUser();
            Transaction incomingTransaction = new Transaction();
            incomingTransaction.setName("Incoming Transaction from " + user.get().getLogin());
            incomingTransaction.setDescription(transaction.getDescription());
            incomingTransaction.setOriginalAmount(transaction.getOriginalAmount());
            incomingTransaction.setAmount(transaction.getAmount());
            incomingTransaction.setCurrency(transaction.getCurrency());
            incomingTransaction.setAddToReports(false);
            incomingTransaction.setAttachment(transaction.getAttachment());
            incomingTransaction.setDateAdded(transaction.getDateAdded());
            incomingTransaction.setIncomingTransaction(true);
            incomingTransaction.setState(TransactionState.PENDING_APPROVAL);
            incomingTransaction.setSourceUser(receivingUser);
            incomingTransaction.setScheduled(false);
            incomingTransaction.setTransactionType(TransactionType.SHARED);
            List<Wallet> wallets = walletRepository.findAllByUser(receivingUser);
            if (wallets.get(0) != null) {
                incomingTransaction.setWallet(wallets.get(0));
            } else {
                throw new BadRequestAlertException("User has no wallets", ENTITY_NAME, "nowallets");
            }

            if (transaction.getMovementType().equals(MovementType.EXPENSE)) {
                incomingTransaction.setMovementType(MovementType.INCOME);
            } else {
                incomingTransaction.setMovementType(MovementType.EXPENSE);
            }
            System.out.println(incomingTransaction.toString());
            transactionRepository.save(incomingTransaction);
            return save(transaction);
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

                            if (
                                transaction.getTransactionType() == TransactionType.API ||
                                transaction.getTransactionType() == TransactionType.EMAIL
                            ) {
                                if (transaction.getOriginalAmount() == existingTransaction.getOriginalAmount()) {
                                    existingTransaction.setOriginalAmount(transaction.getOriginalAmount());
                                    existingTransaction.setAmount(transaction.getAmount());
                                } else {
                                    if (transaction.getCurrency().equals(wallet.getCurrency())) {
                                        existingTransaction.setAmount(transaction.getOriginalAmount());
                                    } else {
                                        Optional<Currency> colon = this.currencyRepository.findOneByCode("CRC");
                                        double tipoCambioColonDolar = 0.0;
                                        if (colon.isPresent()) {
                                            tipoCambioColonDolar = colon.get().getConversionRate();
                                        } else {
                                            throw new BadRequestAlertException("Currency 'CRC' was not found", ENTITY_NAME, "");
                                        }

                                        double transactionInColones = transaction.getAmount() / transaction.getOriginalAmount();
                                        double transactionInDollars = transactionInColones / tipoCambioColonDolar * transaction.getAmount();
                                        transaction.setAmount(transactionInDollars * wallet.getCurrency().getConversionRate());
                                    }
                                }
                            } else {
                                if (transaction.getCurrency().equals(wallet.getCurrency())) {
                                    existingTransaction.setAmount(transaction.getOriginalAmount());
                                } else {
                                    double transactionInDollars =
                                        transaction.getOriginalAmount() / transaction.getCurrency().getConversionRate();
                                    existingTransaction.setAmount(transactionInDollars * wallet.getCurrency().getConversionRate());
                                }
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

    @Transactional
    public Transaction saveXML(TiqueteElectronico tiqueteElectronico) {
        double currentBalance;
        Transaction transaction = new Transaction();
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        log.debug("Request to save Transaction : {}", transaction);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            List<Wallet> wallets = walletRepository.findAllByUser(userDetails.get());
            transaction.setSourceUser(userDetails.get());
            transaction.setTransactionType(TransactionType.API);
            transaction.setIncomingTransaction(false);
            transaction.setScheduled(false);
            transaction.setWallet(wallets.get(0));
            transaction.setDateAdded(tiqueteElectronico.getFechaEmision().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            transaction.setOriginalAmount(tiqueteElectronico.getResumenFactura().getTotalComprobante());
            ResumenFactura resumenFactura = tiqueteElectronico.getResumenFactura();
            CodigoTipoMoneda codigoTipoMoneda = resumenFactura.getCodigoTipoMoneda();
            String currencyCode = codigoTipoMoneda.getCodigoMoneda();
            Optional<Currency> currency = this.currencyRepository.findOneByCode(currencyCode);
            Optional<Currency> colon = this.currencyRepository.findOneByCode("CRC");
            transaction.setAddToReports(true);
            if (currency.isPresent()) {
                transaction.setCurrency(currency.get());
            } else {
                transaction.setCurrency(null);
            }

            double tipoCambioColonDolar = 0.0;
            if (colon.isPresent()) {
                tipoCambioColonDolar = colon.get().getConversionRate();
            } else {
                throw new BadRequestAlertException("Currency 'CRC' was not found", ENTITY_NAME, "");
            }

            if (
                wallets
                    .get(0)
                    .getCurrency()
                    .getCode()
                    .equals(tiqueteElectronico.getResumenFactura().getCodigoTipoMoneda().getCodigoMoneda())
            ) {
                transaction.setAmount(transaction.getOriginalAmount());
            } else {
                double transactionInColones =
                    transaction.getOriginalAmount() * tiqueteElectronico.getResumenFactura().getCodigoTipoMoneda().getTipoCambio();
                double transactionInDollars = transactionInColones / tipoCambioColonDolar;
                transaction.setAmount(transactionInDollars * wallets.get(0).getCurrency().getConversionRate());
            }
            transaction.setMovementType(MovementType.EXPENSE);
            transaction.setState(TransactionState.PENDING_APPROVAL);
            String nombreComercio = tiqueteElectronico.getEmisor().getNombreComercial().equals("")
                ? tiqueteElectronico.getEmisor().getNombre()
                : tiqueteElectronico.getEmisor().getNombreComercial();
            transaction.setName("Compra a " + nombreComercio);

            String detalleResumen = "";

            for (LineaDetalle linea : tiqueteElectronico.getDetalleServicio().getLineasDetalles()) {
                detalleResumen += linea.getDetalle() + " | x" + linea.getCantidad() + " \n";
            }
            if (detalleResumen.length() >= 255) detalleResumen = detalleResumen.substring(0, 254);
            transaction.setDescription(detalleResumen);

            return transactionRepository.save(transaction);
        } else {
            throw new BadRequestAlertException("Los administradores no pueden crear transacciones", ENTITY_NAME, "nopermission");
        }
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
        if (existingTransaction.get().getState().equals(TransactionState.PENDING_APPROVAL)) return;
        if (existingTransaction.get().getMovementType().equals(MovementType.EXPENSE)) {
            wallet.setBalance(wallet.getBalance() + existingTransaction.get().getAmount());
        } else {
            wallet.setBalance(wallet.getBalance() - existingTransaction.get().getAmount());
        }
        walletRepository.save(wallet);
        transactionRepository.deleteById(id);
    }
}
