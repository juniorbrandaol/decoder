package com.ead.notfication.services.impl;

import com.ead.notfication.enuns.NotificationStatus;
import com.ead.notfication.models.NotificationModel;
import com.ead.notfication.repositories.NotificationRepository;
import com.ead.notfication.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    final NotificationRepository notificationRepository;
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
      this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationModel saveNotification(NotificationModel notificationModel) {
      return notificationRepository.save(notificationModel);
    }

   @Override
   public Page<NotificationModel> findAllNotificationsByUser(UUID userId, Pageable pageable) {
      return notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED,pageable);
   }

  @Override
  public Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
    return notificationRepository.findByNotificationIdAndUserId(notificationId,userId);
  }
}
