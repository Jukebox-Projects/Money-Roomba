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
