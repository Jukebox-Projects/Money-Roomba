package com.moneyroomba.repository;

import com.moneyroomba.domain.Event;
import com.moneyroomba.domain.UserDetails;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query("SELECT e FROM Event e WHERE e.user.id =:userId AND e.notification.opened =:opened ORDER BY e.dateAdded DESC")
    List<Event> findAllByNotificationStatus(@Param("userId") Long userId, @Param("opened") Boolean opened);

    List<Event> findAllByUserOrderByDateAddedDesc(UserDetails user);
}
