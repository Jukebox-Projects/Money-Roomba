package com.moneyroomba.service;

import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.dto.reports.TransactionsByCategoryDTO;
import com.moneyroomba.service.dto.reports.WalletBalanceReportDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportsService {

    private final Logger log = LoggerFactory.getLogger(ReportsService.class);

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public ReportsService(WalletRepository walletRepository, TransactionRepository transactionRepository, UserService userService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    /**
     * Get report with expenses and income total by wallet
     *
     * @return the report data needed in the front end graph.
     */
    @Transactional(readOnly = true)
    public List<WalletBalanceReportDTO> getBalancebyWallet(Long id) {
        log.debug("Request to get balance by wallet report");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        return transactionRepository.getWalletBalanceReport(user.get().getId(), id, true, TransactionState.NA);
    }

    /**
     * Get report with expenses and income total from all wallets
     *
     * @return the report data needed in the front end graph.
     */
    @Transactional(readOnly = true)
    public List<WalletBalanceReportDTO> getBalanceAllWallets() {
        log.debug("Request to get balance report for all wallets");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        return transactionRepository.getWalletBalanceReport(user.get().getId(), 13L, true, TransactionState.NA);
        // return walletRepository.findAll();
    }
    /**
     * Get report with expenses and income by category by wallet
     *
     * @return the report data needed in the front end graph.
     */
    /*
    @Transactional(readOnly = true)
    public List<TransactionsByCategoryDTO> getTransactionsByCategoryByWallet(Long id) {
        log.debug("Request to get a transaction by category balance report per wallet");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        return transactionRepository.getTransactionByCategoryReport(user.get().getId(), id, false, TransactionState.NA);
    }*/

    /**
     * Get report with expenses and income by category from all wallets
     *
     * @return the report data needed in the front end graph.
     */
    /*
    @Transactional(readOnly = true)
    public List<TransactionsByCategoryDTO> getTransactionsByCategory() {
        log.debug("Request to get a transaction by category balance report for all wallets");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        // Se debe modificar para no recibir un ID de wallet y hacerlo por todos los wallets
        return transactionRepository.getTransactionByCategoryReport(user.get().getId(), 0L, false, TransactionState.NA);
    }*/
}
