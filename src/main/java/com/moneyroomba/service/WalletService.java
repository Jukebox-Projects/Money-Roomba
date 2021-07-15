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
        // if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
        //     Optional<User> user = userRepository.findOneByLogin(
        //         SecurityUtils.getCurrentUserLogin().get()//.orElseThrow(() -> new Exception("Current user login not found"))
        //     );
        //     Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        //     wallet.setUser(userDetails.get());
        //     Wallet result = walletRepository.save(wallet);
        //     return result;
        // } else {
        //     throw new Exception("Admins cannot register wallets");
        // }
        return walletRepository.save(wallet);
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
        return walletRepository.findAll();
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
