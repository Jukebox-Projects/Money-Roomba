package com.moneyroomba.repository;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.Wallet;
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
}
