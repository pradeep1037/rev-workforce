package com.revworkforce.model;

import java.time.LocalDateTime;

public class Users {

    private int userId;
    private String email;
    private String passwordHash;
    private String role;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

    public Users() {}

    public Users(String email, String passwordHash, String role,
                 boolean isActive, LocalDateTime createdAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Users(int userId, String email, String role,
                 boolean isActive, LocalDateTime lastLogin,
                 LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", lastLogin=" + lastLogin +
                ", createdAt=" + createdAt +
                '}';
    }
}
