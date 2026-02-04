package com.revworkforce.model;

import java.time.LocalDateTime;

public class Announcements {

    private int announcementId;
    private String title;
    private String message;
    private int createdBy;
    private LocalDateTime createdAt;

    public Announcements() {}

    public Announcements(String title, String message,
                         int createdBy, LocalDateTime createdAt) {
        this.title = title;
        this.message = message;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Announcements(int announcementId, String title, String message,
                         int createdBy, LocalDateTime createdAt) {
        this.announcementId = announcementId;
        this.title = title;
        this.message = message;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public int getAnnouncementId() { return announcementId; }
    public void setAnnouncementId(int announcementId) { this.announcementId = announcementId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Announcements{" +
                "announcementId=" + announcementId +
                ", title='" + title + '\'' +
                ", createdBy=" + createdBy +
                '}';
    }
}
