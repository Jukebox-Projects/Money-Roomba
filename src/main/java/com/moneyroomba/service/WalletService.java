package com.moneyroomba.service;

import com.moneyroomba.domain.*;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.repository.*;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.procedure.internal.Util.ResultClassesResolutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Wallet}.
 */
@Service
@Transactional
public class WalletService {

    private static class WalletServiceException extends RuntimeException {

        private WalletServiceException(String message) {
            super(message);
        }
    }

    private static final String ENTITY_NAME = "wallet";

    private final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final TransactionRepository transactionRepository;

    private final EventRepository eventRepository;

    private final UserService userService;

    private final ScheduledTransactionRepository scheduledTransactionRepository;

    public WalletService(
        WalletRepository walletRepository,
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        TransactionRepository transactionRepository,
        EventRepository eventRepository,
        UserService userService,
        ScheduledTransactionRepository scheduledTransactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.transactionRepository = transactionRepository;
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.scheduledTransactionRepository = scheduledTransactionRepository;
    }

    /**
     * Save a wallet.
     *
     * @param wallet the entity to save.
     * @return the persisted entity.
     */
    public Wallet save(Wallet wallet) {
        log.debug("Request to save Wallet : {}", wallet);
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(
                    () ->
                        new BadRequestAlertException(
                            "Current user has no details on its account, could not complete action",
                            ENTITY_NAME,
                            "nonuserfound"
                        )
                )
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        if (wallet.getId() == null) {
            wallet.setUser(userDetails.get());
            List<Wallet> userWallets = walletRepository.findAllByUser(userDetails.get());
            if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PREMIUM_USER)) {
                if (userWallets.size() >= 3) {
                    throw new BadRequestAlertException(
                        "User has reached the max amount of wallets that his accounts can handle.",
                        ENTITY_NAME,
                        "nomorewallets"
                    );
                } else {
                    createEvent(EventType.CREATE);
                    return walletRepository.save(wallet);
                }
            } else {
                createEvent(EventType.CREATE);
                return walletRepository.save(wallet);
            }
        } else {
            Optional<Wallet> existingWallet = walletRepository.findById(wallet.getId());
            if (!userService.currentUserIsAdmin() && !userDetails.get().equals(existingWallet.get().getUser())) {
                throw new BadRequestAlertException("You cannot access or modify this wallet's information", ENTITY_NAME, "walletnoaccess");
            }
            wallet.setUser(existingWallet.get().getUser());
            createEvent(EventType.UPDATE);
            return walletRepository.save(wallet);
        }
    }

    /**
     * Partially update a wallet.
     *
     * @param wallet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Wallet> partialUpdate(Wallet wallet) {
        log.debug("Request to partially update Wallet : {}", wallet);
        createEvent(EventType.UPDATE);
        return walletRepository
            .findById(wallet.getId())
            .map(
                existingWallet -> {
                    if (wallet.getName() != null) {
                        existingWallet.setName(wallet.getName());
                    }
                    if (wallet.getDescription() != null) {
                        existingWallet.setDescription(wallet.getDescription());
                    }
                    if (wallet.getInReports() != null) {
                        existingWallet.setInReports(wallet.getInReports());
                    }
                    if (wallet.getIsActive() != null) {
                        existingWallet.setIsActive(wallet.getIsActive());
                    }
                    if (wallet.getBalance() != null) {
                        existingWallet.setBalance(wallet.getBalance());
                    }
                    if (wallet.getIcon() != null) {
                        existingWallet.setIcon(wallet.getIcon());
                    }

                    return existingWallet;
                }
            )
            .map(walletRepository::save);
    }

    /**
     * Get all the wallets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Wallet> findAll() {
        log.debug("Request to get all Wallets");
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        List<Wallet> entityList = walletRepository.findAll();
        List<Wallet> res = new ArrayList<Wallet>();
        if (
            (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) &&
            (
                SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER) ||
                SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PREMIUM_USER)
            )
        ) {
            if (userDetails.isPresent()) {
                for (Wallet wallet : entityList) {
                    if (wallet.getUser() == null) {} else {
                        if (wallet.getUser().equals(userDetails.get())) {
                            res.add(wallet);
                            System.out.println(res.size());
                        }
                    }
                }
            }
        } else {
            if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
                res = entityList;
            }
        }
        return res;
    }

    @Transactional(readOnly = true)
    public List<Wallet> findAllByUserId(UserDetails user) {
        log.debug("Request to get all Wallets for one user");
        return walletRepository.findAllByUser(user);
    }

    /**
     * Get one wallet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Wallet> findOne(Long id) {
        log.debug("Request to get Wallet : {}", id);
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (!userService.currentUserIsAdmin() && !userDetails.get().equals(wallet.get().getUser())) {
            throw new BadRequestAlertException("You cannot access or modify this wallet's information", ENTITY_NAME, "walletnoaccess");
        }
        return wallet;
    }

    @Transactional(readOnly = true)
    public Optional<Wallet> findOneById(Long id) {
        log.debug("Request to get Wallet : {}", id);
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (!userService.currentUserIsAdmin() && !userDetails.get().equals(wallet.get().getUser())) {
            throw new BadRequestAlertException("You cannot access or modify this wallet's information", ENTITY_NAME, "walletnoaccess");
        }
        return wallet;
    }

    /**
     * Delete the wallet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Wallet : {}", id);
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        Optional<Wallet> wallet = walletRepository.findById(id);
        List<Transaction> transactions = transactionRepository.findAllByWallet(wallet.get());
        List<ScheduledTransaction> scheduledTransactions = scheduledTransactionRepository.findAllByWallet(wallet.get());
        if (!userService.currentUserIsAdmin() && !userDetails.get().equals(wallet.get().getUser())) {
            throw new BadRequestAlertException("You cannot access or modify this wallet's information", ENTITY_NAME, "walletnoaccess");
        }
        if (!transactions.isEmpty()) {
            for (Transaction t : transactions) {
                transactionRepository.delete(t);
            }
        }
        if (!scheduledTransactions.isEmpty()) {
            for (ScheduledTransaction t : scheduledTransactions) {
                scheduledTransactionRepository.delete(t);
            }
        }
        createEvent(EventType.DELETE);
        walletRepository.deleteById(id);
    }

    /**
     * Create event.
     *
     * @param eventType of the entity.
     */
    public void createEvent(EventType eventType) {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, ""))
        );

        Event event = new Event();
        event.setEventType(eventType);
        event.setDateAdded(LocalDate.now());
        event.setSourceId(user.get().getId());
        event.setSourceEntity(SourceEntity.WALLET);
        event.setUserName(user.get().getFirstName());
        event.setUserLastName(user.get().getLastName());
        System.out.println(event);
        eventRepository.save(event);
    }
}
