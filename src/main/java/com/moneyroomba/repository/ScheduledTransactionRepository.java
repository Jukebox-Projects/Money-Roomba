package com.moneyroomba.repository;

import com.moneyroomba.domain.ScheduledTransaction;
import com.moneyroomba.domain.Wallet;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduledTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledTransactionRepository
    extends JpaRepository<ScheduledTransaction, Long>, JpaSpecificationExecutor<ScheduledTransaction> {
    public List<ScheduledTransaction> findAllByWallet(Wallet wallet);
}
