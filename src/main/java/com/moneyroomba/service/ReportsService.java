package com.moneyroomba.service;

import static java.time.temporal.ChronoUnit.DAYS;

import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.dto.reports.StartEndDatesDTO;
import com.moneyroomba.service.dto.reports.WalletBalanceReportDTO;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private final CurrencyRepository currencyRepository;

    private final UserService userService;

    private final String DEFAULT_CURRENCY_CODE = "USD";

    public ReportsService(
        WalletRepository walletRepository,
        TransactionRepository transactionRepository,
        CurrencyRepository currencyRepository,
        UserService userService
    ) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.currencyRepository = currencyRepository;
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
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent() && user.isPresent()) {
            if (wallet.get().getInReports()) {
                return transactionRepository.getWalletBalanceReport(user.get().getId(), id, true, TransactionState.NA);
            }
        }
        return null;
    }

    /**
     * Get report with expenses and income total from all wallets
     *
     * @return the report data needed in the front end graph.
     */
    @Transactional(readOnly = true)
    public List<WalletBalanceReportDTO> getBalanceAllWallets(String startDate, String endDate) {
        log.debug("Request to get balance report for all wallets");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<WalletBalanceReportDTO> results = null;
        if (user.isPresent()) {
            Long daysInBetween = DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
            results =
                transactionRepository.getAllWalletBalanceReport(user.get().getId(), true, TransactionState.NA, daysInBetween.intValue());

            if (!results.isEmpty()) {
                if (!sameCurrencyForAllWallets(results)) {
                    Currency defaultCurrency = getDefaultCurrency();
                    results = convertConversionForAllWallets(results, defaultCurrency);
                }
            }
        }
        return results;
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

    private Boolean sameCurrencyForAllWallets(List<WalletBalanceReportDTO> results) {
        String previousCurrencyCode = null;
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getCurrency().getCode().equals(previousCurrencyCode) || i == 0) {
                previousCurrencyCode = results.get(i).getCurrency().getCode();
            } else {
                return false;
            }
        }
        return true;
    }

    private Currency getDefaultCurrency() {
        return currencyRepository.findOneByCode(DEFAULT_CURRENCY_CODE).orElse(null);
    }

    private List<WalletBalanceReportDTO> convertConversionForAllWallets(List<WalletBalanceReportDTO> results, Currency targetCurrency) {
        ArrayList<WalletBalanceReportDTO> formatedResults = new ArrayList<>();
        formatedResults.add(0, new WalletBalanceReportDTO(0D, MovementType.INCOME, targetCurrency));
        formatedResults.add(1, new WalletBalanceReportDTO(0D, MovementType.EXPENSE, targetCurrency));

        results.forEach(
            currentItem -> {
                currentItem.setTotal(convertAmount(currentItem.getCurrency(), targetCurrency, currentItem.getTotal()));
                if (currentItem.getMovementType().equals(MovementType.INCOME)) {
                    formatedResults.get(0).setTotal(formatedResults.get(0).getTotal() + currentItem.getTotal());
                } else {
                    formatedResults.get(1).setTotal(formatedResults.get(1).getTotal() + currentItem.getTotal());
                }
            }
        );
        return formatedResults;
    }

    private Double convertAmount(Currency sourceCurrency, Currency targetCurrency, Double amount) {
        return (amount * targetCurrency.getConversionRate()) / sourceCurrency.getConversionRate();
    }
}
