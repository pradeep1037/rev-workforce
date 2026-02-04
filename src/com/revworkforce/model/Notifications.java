package com.revworkforce.model;
import java.time.LocalDateTime;

public class Notifications {

    private int notificationId;
    private int userId;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public Notifications() {}

    public Notifications(int userId, String message, String type,
                         boolean isRead, LocalDateTime createdAt) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Notifications(int notificationId, int userId, String message,
                         String type, boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + notificationId +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", read=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }

}

