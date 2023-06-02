package com.ead.notfication.models;

import com.ead.notfication.enuns.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_NOTIFICATIONS")
public class NotificationModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;
    @Column(nullable = false)
    private  UUID userId;
    @Column(nullable = false,length = 150)
    private String title;
    @Column(nullable = false)
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime criationDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    public NotificationModel(){}

    public UUID getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String menssage) {
        this.message = menssage;
    }

    public LocalDateTime getCriationDate() {
        return criationDate;
    }

    public void setCriationDate(LocalDateTime criationDate) {
        this.criationDate = criationDate;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
