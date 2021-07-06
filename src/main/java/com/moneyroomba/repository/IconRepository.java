package com.moneyroomba.repository;

import com.moneyroomba.domain.Icon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Icon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IconRepository extends JpaRepository<Icon, Long>, JpaSpecificationExecutor<Icon> {}
