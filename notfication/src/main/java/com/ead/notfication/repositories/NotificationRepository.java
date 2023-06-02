package com.ead.notfication.repositories;

import com.ead.notfication.enuns.NotificationStatus;
import com.ead.notfication.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {

  Page<NotificationModel> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, Pageable pageable);
  Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId);

}
