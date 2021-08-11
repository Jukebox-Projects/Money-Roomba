package com.moneyroomba.repository;

import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserDetails entity.
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    @Query(
        value = "select distinct userDetails from UserDetails userDetails left join fetch userDetails.targetContacts",
        countQuery = "select count(distinct userDetails) from UserDetails userDetails"
    )
    Page<UserDetails> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userDetails from UserDetails userDetails left join fetch userDetails.targetContacts")
    List<UserDetails> findAllWithEagerRelationships();

    @Query("select userDetails from UserDetails userDetails left join fetch userDetails.targetContacts where userDetails.id =:id")
    Optional<UserDetails> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<UserDetails> findOneByInternalUserId(Long id);
    Optional<UserDetails> findOneByInternalUser(User user);
    Optional<UserDetails> findOneByApiKey(String key);
    Optional<UserDetails> findOneById(Long id);
}
