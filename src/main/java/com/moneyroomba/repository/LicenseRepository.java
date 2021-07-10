package com.moneyroomba.repository;

import com.moneyroomba.domain.License;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the License entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LicenseRepository extends JpaRepository<License, Long>, JpaSpecificationExecutor<License> {}
