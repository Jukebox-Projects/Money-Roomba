package com.moneyroomba.repository;

import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.Wallet;
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
}
