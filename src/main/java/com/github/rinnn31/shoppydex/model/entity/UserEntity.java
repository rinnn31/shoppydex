package com.github.rinnn31.shoppydex.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "User")
public class UserEntity {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long userId;

    @Column(name = "Username", nullable = false, unique = true)
    private String username;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Role", nullable = false)
    private String role;

    @Column(name = "Points", nullable = false)
    private Double points;

    @Column(name = "IsVerified", nullable = false)
    private Boolean IsVerified;

    @Column(name = "ActiveToken")
    private String activeToken;

    public UserEntity() {
        // Default constructor for JPA
    }

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = ROLE_USER;
        this.points = 0.0;
        this.IsVerified = false;
        this.activeToken = null;
    }

    public Long getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (ROLE_ADMIN.equals(role) || ROLE_USER.equals(role)) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public boolean isVerified() {
        return IsVerified;
    }

    public void setVerified(boolean isVerified) {
        IsVerified = isVerified;
    }

    public String getActiveToken() {
        return activeToken;
    }

    public void setActiveToken(String activeToken) {
        this.activeToken = activeToken;
    }
}
