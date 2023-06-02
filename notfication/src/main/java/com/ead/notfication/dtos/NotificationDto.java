package com.ead.notfication.dtos;

import com.ead.notfication.enuns.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public class NotificationDto {

  @NotNull
  private NotificationStatus notificationStatus;

  public NotificationStatus getNotificationStatus() {
    return notificationStatus;
  }

  public void setNotificationStatus(NotificationStatus notificationStatus) {
    this.notificationStatus = notificationStatus;
  }
}
