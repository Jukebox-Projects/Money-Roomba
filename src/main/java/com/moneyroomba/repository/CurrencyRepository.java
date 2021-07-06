package com.moneyroomba.repository;

import com.moneyroomba.domain.Currency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {}
