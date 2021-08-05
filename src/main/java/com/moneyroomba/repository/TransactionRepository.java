package com.moneyroomba.repository;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.TransactionState;
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
        "SELECT count(id) FROM Transaction t WHERE (date_added between ?2 AND ?3) AND (transaction_type = 'EMAIL' OR transaction_type = 'API') and source_user_id = ?1"
    )
    int countImportedTransactions(Long source_user_id, LocalDate startOfMonth, LocalDate endOfMonth);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.WalletBalanceReportDTO(sum(tr.amount), tr.movementType, tr.wallet, tr.currency) " +
        "FROM Transaction tr " +
        "WHERE tr.sourceUser.id = ?1 AND" +
        " tr.wallet.id = ?2 AND" +
        " tr.addToReports = ?3 AND" +
        " tr.state = ?4 AND" +
        " abs(datediff(tr.dateAdded, CURRENT_DATE)) BETWEEN 0 AND 30" +
        " GROUP BY tr.movementType, tr.wallet, tr.currency " +
        " ORDER BY tr.wallet, tr.movementType"
    )
    public List<WalletBalanceReportDTO> getWalletBalanceReport(Long userId, Long walletId, Boolean addToReports, TransactionState state);
    /*
    //Agregar query - Reporte de Javier
    public List<TransactionsByCategoryDTO> getTransactionByCategoryReport(
        Long userId,
        Long walletId,
        Boolean addToReports,
        TransactionState state
    );*/
}
