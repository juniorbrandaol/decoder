package com.ead.notfication.services.impl;

import com.ead.notfication.repositories.NotificationRepository;
import com.ead.notfication.services.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    final NotificationRepository notificationRepository;
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


}
