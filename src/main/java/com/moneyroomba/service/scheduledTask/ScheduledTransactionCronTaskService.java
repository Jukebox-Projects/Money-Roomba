package com.moneyroomba.service.scheduledTask;

import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.ScheduledTransactionRepository;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.TransactionService;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTransactionCronTaskService {

    ScheduledTransactionRepository scheduledTransactionRepository;
    WalletRepository walletRepository;
    TransactionRepository transactionRepository;

    public ScheduledTransactionCronTaskService(
        ScheduledTransactionRepository scheduledTransactionRepository,
        WalletRepository walletRepository,
        TransactionRepository transactionRepository
    ) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 30 1 * * *")
    public void registerScheduledTransactions() throws Exception {
        List<ScheduledTransaction> allTransactions = scheduledTransactionRepository.findAll();
        LocalDate today = LocalDate.now();
        for (ScheduledTransaction t : allTransactions) {
            if (
                (today.isAfter(t.getStartDate()) || today.isEqual(t.getStartDate())) &&
                (t.getEndDate() == null || (today.isBefore(t.getEndDate()) || today.isEqual(t.getStartDate())))
            ) {
                switch (t.getRecurringType()) {
                    case DAILY:
                        saveTransaction(t);
                        break;
                    case WEEKLY:
                        if ((today.getDayOfWeek().getValue() - 1) == t.getDayOfWeek()) {
                            saveTransaction(t);
                        }
                        break;
                    case MONTHLY:
                        if (today.getDayOfMonth() == t.getDayOfMonth()) {
                            saveTransaction(t);
                        }
                        break;
                    case YEARLY:
                        if (today.getDayOfMonth() == t.getDayOfMonth() && (today.getMonthValue() - 1) == t.getMonthOfYear()) {
                            saveTransaction(t);
                        }
                        break;
                }
            }
        }
    }

    public void saveTransaction(ScheduledTransaction scheduledTransaction) {
        Transaction transaction = new Transaction();
        transaction.setState(TransactionState.PENDING_APPROVAL);
        transaction.setName(scheduledTransaction.getName());
        transaction.setDescription(scheduledTransaction.getDescription());
        transaction.setOriginalAmount(scheduledTransaction.getOriginalAmount());
        transaction.setSourceUser(scheduledTransaction.getSourceUser());
        transaction.setTransactionType(TransactionType.SCHEDULED);
        transaction.setScheduled(true);
        transaction.setIncomingTransaction(true);
        transaction.setDateAdded(LocalDate.now());
        transaction.setCurrency(scheduledTransaction.getCurrency());
        transaction.setAddToReports(scheduledTransaction.getAddToReports());
        transaction.setMovementType(scheduledTransaction.getMovementType());
        transaction.setCategory(scheduledTransaction.getCategory());

        //double currentBalance;
        Optional<Wallet> completeWallet = walletRepository.findById(scheduledTransaction.getWallet().getId());
        if (transaction.getCurrency().equals(completeWallet.get().getCurrency())) {
            transaction.setAmount(transaction.getOriginalAmount());
        } else {
            double transactionInDollars = transaction.getOriginalAmount() / transaction.getCurrency().getConversionRate();
            transaction.setAmount(transactionInDollars * completeWallet.get().getCurrency().getConversionRate());
        }
        transaction.setWallet(completeWallet.get());
        //        if (transaction.getMovementType().equals(MovementType.EXPENSE)) {
        //            currentBalance = completeWallet.get().getBalance();
        //            currentBalance = currentBalance - transaction.getAmount();
        //            completeWallet.get().setBalance(currentBalance);
        //            walletRepository.save(completeWallet.get());
        //        } else {
        //            currentBalance = completeWallet.get().getBalance();
        //            currentBalance = currentBalance + transaction.getAmount();
        //            completeWallet.get().setBalance(currentBalance);
        //            walletRepository.save(completeWallet.get());
        //        }
        transactionRepository.save(transaction);
    }
}
