package com.moneyroomba.repository;

import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.service.dto.reports.WalletStatisticDTO;
import com.moneyroomba.service.dto.reports.WalletTotalBalanceReportDTO;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Wallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
    public List<Wallet> findAllByUser(UserDetails user);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.WalletTotalBalanceReportDTO(tr.balance, tr.currency) " +
        "FROM Wallet tr " +
        "WHERE tr.user.id = ?1 AND " +
        "tr.inReports = ?2 AND " +
        "tr.isActive = ?3 "
    )
    public List<WalletTotalBalanceReportDTO> getTotalBalance(Long userId, Boolean addToReports, Boolean isActive);

    @Query(
        value = "SELECT new com.moneyroomba.service.dto.reports.WalletStatisticDTO(tr.balance, tr.name, tr.currency) " +
        "FROM Wallet tr " +
        "WHERE tr.user.id = ?1 AND " +
        "tr.inReports = ?2 AND " +
        "tr.isActive = ?3 "
    )
    public List<WalletStatisticDTO> getWalletStatistic(Long userId, Boolean addToReports, Boolean isActive);
}
