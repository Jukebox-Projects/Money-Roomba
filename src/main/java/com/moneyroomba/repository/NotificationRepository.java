package com.moneyroomba.repository;

import com.moneyroomba.domain.Notification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {}
