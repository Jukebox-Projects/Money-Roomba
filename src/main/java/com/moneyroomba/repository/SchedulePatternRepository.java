package com.moneyroomba.repository;

import com.moneyroomba.domain.SchedulePattern;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulePattern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulePatternRepository extends JpaRepository<SchedulePattern, Long> {}
