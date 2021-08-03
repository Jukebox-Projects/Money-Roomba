package com.moneyroomba.repository;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.service.dto.reports.TransactionsByCategoryDTO;
import com.moneyroomba.service.dto.reports.WalletBalanceReportDTO;
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

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.WalletBalanceReportDTO(SUM(tr.amount), tr.movementType, tr.wallet, tr.currency) " +
        "FROM Transaction tr " +
        "WHERE tr.sourceUser.id = ?1 AND" +
        " tr.wallet.id = ?2 AND" +
        " tr.addToReports = ?3 AND" +
        " tr.state = ?4 AND" +
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
        "tr.state = ?3 AND " +
        "ABS(DATEDIFF(tr.dateAdded, CURRENT_TIMESTAMP)) BETWEEN 0 AND ?4 " +
        "GROUP BY tr.movementType, tr.wallet, tr.currency " +
        "ORDER BY tr.wallet, tr.movementType DESC, tr.currency"
    )
    public List<WalletBalanceReportDTO> getAllWalletBalanceReport(
        Long userId,
        Boolean addToReports,
        TransactionState state,
        Integer daysInBetween
    );
    /*
    //Agregar query - Reporte de Javier
    public List<TransactionsByCategoryDTO> getTransactionByCategoryReport(
        Long userId,
        Long walletId,
        Boolean addToReports,
        TransactionState state
    );*/
}
