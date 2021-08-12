package com.moneyroomba.repository;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.service.dto.reports.TransactionCountReportDTO;
import com.moneyroomba.service.dto.reports.TransactionsByCategoryDTO;
import com.moneyroomba.service.dto.reports.WalletBalanceReportDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    public List<Transaction> findAllByWallet(Wallet wallet);

    //SELECT count(*) FROM bzavahbosdx1rt1p.t_transaction WHERE MONTH(date_added) = MONTH(CURRENT_DATE())
    //AND YEAR(date_added) = YEAR(CURRENT_DATE()) and (transaction_type = 'EMAIL' OR transaction_type = 'API') and source_user_id = 11;
    @Query(
        "SELECT count(t.id) FROM Transaction t WHERE (t.dateAdded between ?2 AND ?3) AND (t.transactionType = 'EMAIL' OR t.transactionType = 'API') and t.sourceUser.id = ?1"
    )
    int countImportedTransactions(Long source_user_id, LocalDate startOfMonth, LocalDate endOfMonth);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.WalletBalanceReportDTO(SUM(tr.amount), tr.movementType, tr.wallet, tr.currency) " +
        "FROM Transaction tr " +
        "WHERE tr.sourceUser.id = ?1 AND" +
        " tr.wallet.id = ?2 AND" +
        " tr.addToReports = ?3 AND" +
        " tr.state = 'NA' OR tr.state = 'ACCEPTED' AND" +
        " ABS(datediff(tr.dateAdded, CURRENT_DATE)) BETWEEN 0 AND 30" +
        " GROUP BY tr.movementType, tr.wallet, tr.currency " +
        " ORDER BY tr.wallet, tr.movementType DESC"
    )
    public List<WalletBalanceReportDTO> getWalletBalanceReport(Long userId, Long walletId, Boolean addToReports, TransactionState state);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.WalletBalanceReportDTO(SUM(tr.amount), tr.movementType, tr.wallet, tr.currency) " +
        "FROM Transaction tr " +
        "WHERE tr.sourceUser.id = ?1 AND " +
        "tr.addToReports = ?2 AND " +
        "tr.state = 'NA' OR tr.state = 'ACCEPTED' AND " +
        "ABS(DATEDIFF(tr.dateAdded, CURRENT_TIMESTAMP)) BETWEEN 0 AND ?3 " +
        "GROUP BY tr.movementType, tr.wallet, tr.currency " +
        "ORDER BY tr.wallet, tr.movementType DESC, tr.currency"
    )
    public List<WalletBalanceReportDTO> getAllWalletBalanceReport(Long userId, Boolean addToReports, Integer daysInBetween);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.TransactionCountReportDTO(COUNT(tr.id), tr.movementType) " +
        "FROM Transaction tr " +
        "WHERE tr.sourceUser.id = ?1 AND " +
        "tr.addToReports = ?2 AND " +
        "tr.state = 'NA' OR tr.state = 'ACCEPTED' AND " +
        "ABS(DATEDIFF(tr.dateAdded, CURRENT_TIMESTAMP)) BETWEEN 0 AND ?3 " +
        "GROUP BY tr.movementType " +
        "ORDER BY tr.movementType DESC"
    )
    public List<TransactionCountReportDTO> getTransactionCount(Long userId, Boolean addToReports, Integer daysInBetween);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.TransactionsByCategoryDTO( SUM(tr.amount), COUNT(tr.id),tr.category, tr.movementType, tr.currency ) " +
        "FROM Transaction tr " +
        "WHERE tr.sourceUser.id = ?1 AND " +
        "tr.addToReports = ?2 AND " +
        "tr.state = ?3 AND " +
        "tr.movementType = ?4 " +
        "GROUP BY tr.movementType, tr.category, tr.currency " +
        "ORDER BY tr.movementType DESC"
    )
    public List<TransactionsByCategoryDTO> getTransactionByCategoryReport(
        Long userId,
        Boolean addToReports,
        TransactionState state,
        MovementType movementType
    );
}
