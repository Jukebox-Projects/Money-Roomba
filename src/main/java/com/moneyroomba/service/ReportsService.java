package com.moneyroomba.service;

import static java.time.temporal.ChronoUnit.DAYS;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.dto.reports.TransactionCountReportDTO;
import com.moneyroomba.service.dto.reports.TransactionsByCategoryDTO;
import com.moneyroomba.service.dto.reports.WalletBalanceReportDTO;
import com.moneyroomba.service.dto.reports.WalletTotalBalanceReportDTO;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportsService {

    private final Logger log = LoggerFactory.getLogger(ReportsService.class);

    private static final String ENTITY_NAME = "report";

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
        } else {
            throw new BadRequestAlertException("Could not find the user", ENTITY_NAME, "nouserfound");
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
        } else {
            throw new BadRequestAlertException("Could not find the user", ENTITY_NAME, "nouserfound");
        }
        return results;
        // return walletRepository.findAll();
    }

    /**
     * Get report with expenses and income total from all wallets
     *
     * @return the report data needed in the front end graph.
     */
    @Transactional(readOnly = true)
    public List<TransactionCountReportDTO> getTransactionCount(String startDate, String endDate) {
        log.debug("Request to get amount of income and expense transactions by user");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<TransactionCountReportDTO> results = null;
        if (user.isPresent()) {
            Long daysInBetween = DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
            results = transactionRepository.getTransactionCount(user.get().getId(), true, TransactionState.NA, daysInBetween.intValue());
        } else {
            throw new BadRequestAlertException("Could not find the user", ENTITY_NAME, "nouserfound");
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

    /**
     * Get report with expenses and income total from all wallets
     *
     * @return the report data needed in the front end graph.
     */
    @Transactional(readOnly = true)
    public List<WalletTotalBalanceReportDTO> getTotalBalance() {
        log.debug("Request to get total balance report for all wallets");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<WalletTotalBalanceReportDTO> results = null;
        if (user.isPresent()) {
            results = walletRepository.getTotalBalance(user.get().getId(), true, true);

            if (!results.isEmpty()) {
                if (!sameCurrencyTotalBalance(results)) {
                    Currency defaultCurrency = getDefaultCurrency();
                    results = convertConversionTotalBalance(results, defaultCurrency);
                }
            }
        } else {
            throw new BadRequestAlertException("Could not find the user", ENTITY_NAME, "nouserfound");
        }
        return results;
        // return walletRepository.findAll();
    }

    private Boolean sameCurrencyTotalBalance(List<WalletTotalBalanceReportDTO> results) {
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

    private List<WalletTotalBalanceReportDTO> convertConversionTotalBalance(
        List<WalletTotalBalanceReportDTO> results,
        Currency targetCurrency
    ) {
        ArrayList<WalletTotalBalanceReportDTO> formatedResults = new ArrayList<>();
        formatedResults.add(0, new WalletTotalBalanceReportDTO(0D, targetCurrency));

        results.forEach(
            currentItem -> {
                currentItem.setTotal(convertAmount(currentItem.getCurrency(), targetCurrency, currentItem.getTotal()));
                formatedResults.get(0).setTotal(formatedResults.get(0).getTotal() + currentItem.getTotal());
            }
        );
        return formatedResults;
    }

    /**
     * Get report with expenses and income by category from all wallets
     *
     * @return the report data needed in the front end graph.
     */
    @Transactional(readOnly = true)
    public List<TransactionsByCategoryDTO> getTransactionsByCategory(int movementType) {
        log.debug("Request to get a transaction by category balance report for all wallets");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<TransactionsByCategoryDTO> results = null;

        MovementType mType;

        if (movementType == 1) {
            mType = MovementType.EXPENSE;
        } else {
            mType = MovementType.INCOME;
        }

        if (user.isPresent()) {
            results = transactionRepository.getTransactionByCategoryReport(user.get().getId(), true, TransactionState.NA, mType);
            if (!results.isEmpty()) {
                if (!sameCurrencyTransactionsByCategory(results)) {
                    Currency defaultCurrency = getDefaultCurrency();
                    results = convertConversionTransactionsByCategory(results, defaultCurrency);
                }
                results = orderTransactionsByCategory(results);
            }
        } else {
            throw new BadRequestAlertException("Could not find the user", ENTITY_NAME, "nouserfound");
        }

        return results;
    }

    public List<TransactionsByCategoryDTO> orderTransactionsByCategory(List<TransactionsByCategoryDTO> allTransactions) {
        List<TransactionsByCategoryDTO> formatedResults = new ArrayList<TransactionsByCategoryDTO>();
        List<TransactionsByCategoryDTO> finalResults = new ArrayList<TransactionsByCategoryDTO>();
        double total = 0;
        TransactionsByCategoryDTO other = new TransactionsByCategoryDTO(0.0, 0L, null, null, currencyRepository.findOneByCode("USD").get());
        for (TransactionsByCategoryDTO categoryGroup : allTransactions) {
            if (formatedResults.isEmpty()) {
                formatedResults.add(categoryGroup);
                total += categoryGroup.getTotal();
            } else {
                for (TransactionsByCategoryDTO transactionsWithBalance : formatedResults) {
                    if (
                        transactionsWithBalance.getCategory().equals(categoryGroup.getCategory()) &&
                        !transactionsWithBalance.equals(categoryGroup)
                    ) {
                        transactionsWithBalance.setTotal(transactionsWithBalance.getTotal() + categoryGroup.getTotal());
                        total += categoryGroup.getTotal();
                    } else {
                        formatedResults.add(categoryGroup);
                        total += categoryGroup.getTotal();
                        break;
                    }
                }
            }
        }

        formatedResults.sort(Comparator.comparing(TransactionsByCategoryDTO::getTotal).reversed());

        for (int i = 0; i < formatedResults.size(); i++) {
            if (i < 3) {
                finalResults.add(formatedResults.get(i));
            } else {
                other.setTotal(other.getTotal() + formatedResults.get(i).getTotal());
                other.setCounter(other.getCounter() + formatedResults.get(i).getCounter());
            }
        }
        finalResults.add(other);

        for (TransactionsByCategoryDTO result : finalResults) {
            double percentage = (result.getTotal() / total) * 100;
            result.setPercentage(percentage);
        }
        return finalResults;
    }

    /*
    @Transactional(readOnly = true)
    public List<TransactionsByCategoryDTO> getTransactionsByCategory(String startDate, String endDate) {
        log.debug("Request to get a transaction by category balance report for all wallets");
        Optional<UserDetails> user = userService.getUserDetailsByLogin();
        List<TransactionsByCategoryDTO> results = null;
        if (user.isPresent()) {
            Long daysInBetween = DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
            results =
                transactionRepository.getTransactionByCategoryReport(
                    user.get().getId(),
                    true,
                    TransactionState.NA,
                    daysInBetween.intValue());
            System.out.println("GET TRANSACTIONS BY CATEGORY");
            System.out.println(results);
            System.out.println("GET TRANSACTIONS BY CATEGORY");
            if (!results.isEmpty()) {
                if (!sameCurrencyTransactionsByCategory(results)) {
                    Currency defaultCurrency = getDefaultCurrency();
                    results = convertConversionTransactionsByCategory(results, defaultCurrency);
                }
            }
        } else {
            throw new BadRequestAlertException("Could not find the user", ENTITY_NAME, "nouserfound");
        }
        System.out.println("GET TRANSACTIONS BY CATEGORY WITH FORMATED");
        System.out.println(results);
        System.out.println("GET TRANSACTIONS BY CATEGORY WITH FORMATED");
        return results;
    }
*/
    private Boolean sameCurrencyTransactionsByCategory(List<TransactionsByCategoryDTO> results) {
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

    private List<TransactionsByCategoryDTO> convertConversionTransactionsByCategory(
        List<TransactionsByCategoryDTO> results,
        Currency targetCurrency
    ) {
        ArrayList<TransactionsByCategoryDTO> formatedResults = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            formatedResults.add(
                i,
                new TransactionsByCategoryDTO(
                    0D,
                    results.get(i).getCounter(),
                    results.get(i).getCategory(),
                    results.get(i).getMovementType(),
                    targetCurrency
                )
            );
            results.get(i).setTotal(convertAmount(results.get(i).getCurrency(), targetCurrency, results.get(i).getTotal()));
            formatedResults.get(i).setTotal(formatedResults.get(0).getTotal() + results.get(i).getTotal());
        }
        return formatedResults;
    }
}
