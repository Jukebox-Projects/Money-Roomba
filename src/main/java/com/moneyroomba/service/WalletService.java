package com.moneyroomba.service;

import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.repository.WalletRepository;
import java.util.List;
import java.util.Optional;
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

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Save a wallet.
     *
     * @param wallet the entity to save.
     * @return the persisted entity.
     */
    public Wallet save(Wallet wallet) {
        log.debug("Request to save Wallet : {}", wallet);
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
