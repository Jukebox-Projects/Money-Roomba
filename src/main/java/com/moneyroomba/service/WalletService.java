package com.moneyroomba.service;

import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
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

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * Save a wallet.
     *
     * @param wallet the entity to save.
     * @return the persisted entity.
     */
    public Wallet save(Wallet wallet) {
        log.debug("Request to save Wallet : {}", wallet);
        int walletCount = 0;
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new WalletServiceException("Current user login not found"))
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        wallet.setUser(userDetails.get());
        List<Wallet> userWallets = walletRepository.findAllByUser(userDetails.get());
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PREMIUM_USER)) {
            if (userWallets.size() >= 3) {
                throw new WalletServiceException("User cannot register more wallets.");
            } else {
                return walletRepository.save(wallet);
            }
        } else {
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
        return walletRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Wallet> findOneById(Long id) {
        log.debug("Request to get Wallet : {}", id);
        return walletRepository.findById(id);
    }

    /**
     * Delete the wallet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Wallet : {}", id);
        walletRepository.deleteById(id);
    }
}
