package com.moneyroomba.repository;

import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findOneByInternalUserId(Long id);
}
