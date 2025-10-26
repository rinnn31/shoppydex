package com.github.rinnn31.shoppydex.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class VerificationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="VerificationId")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UserID", nullable=false, referencedColumnName="UserID")
    private User user;

    @Column(name = "Token", nullable=false)
    private String verificationToken;

    @Column(name = "Action", nullable=false)
    private String action;

    @Column(name = "ExpiredAt", nullable=false)
    private LocalDateTime expiredAt;

    @Column(name = "CreatedAt", nullable=false)
    private LocalDateTime createdAt;

    public VerificationInfo() {
        // Default constructor for JPA
    }

    public VerificationInfo(User user, String action, int validDurationMinutes) {
        this.user = user;
        this.verificationToken = UUID.randomUUID().toString();
        this.action = action;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = this.createdAt.plusMinutes(validDurationMinutes);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getVerificationToken() {
        return verificationToken;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setVerificationCode(String verificationToken) {
        this.verificationToken = verificationToken;
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
