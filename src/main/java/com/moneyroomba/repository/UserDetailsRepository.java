package com.moneyroomba.repository;

import com.moneyroomba.domain.UserDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {}
