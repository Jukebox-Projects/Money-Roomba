package com.moneyroomba.repository;

import com.moneyroomba.domain.SystemSetting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long>, JpaSpecificationExecutor<SystemSetting> {}
