package com.github.rinnn31.shoppydex.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "VerificationInfo")
public class VerificationInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="VerificationId")
    private long id;

    @Column(name = "Username", nullable=false)
    private String username;

    @Column(name = "Code", nullable=false)
    private String code;

    @Column(name = "Action", nullable=false)
    private String action;

    @Column(name = "ExpiredAt", nullable=false)
    private LocalDateTime expiredAt;

    @Column(name = "CreatedAt", nullable=false)
    private LocalDateTime createdAt;

    public VerificationInfoEntity() {
        // Default constructor for JPA
    }

    public VerificationInfoEntity(String username, String action, int validDurationMinutes) {
        this.username = username;
        this.code = String.format("%06d", (int)(Math.random() * 1000000));
        this.action = action;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = this.createdAt.plusMinutes(validDurationMinutes);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCode() {
        return code;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }
}
