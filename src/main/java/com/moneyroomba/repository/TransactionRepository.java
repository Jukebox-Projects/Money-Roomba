package com.moneyroomba.repository;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
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
}
