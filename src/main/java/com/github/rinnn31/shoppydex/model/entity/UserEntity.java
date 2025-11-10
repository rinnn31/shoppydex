package com.github.rinnn31.shoppydex.model.entity;

import java.util.List;

import com.github.rinnn31.shoppydex.model.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "User")
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "UserId")
    private long userId;

    @Column(name = "Username", nullable = false, unique = true)
    private String username;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Role", nullable = false)
    private String role;

    @Column(name = "Points", nullable = false)
    private int points;

    @Column(name = "IsVerified", nullable = false)
    private boolean IsVerified;

    @Column(name = "ActiveToken")
    private String activeToken;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderItemEntity> orders;

    public UserEntity() {
        // Default constructor for JPA
    }

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER.name();
        this.points = 0;
        this.IsVerified = false;
        this.activeToken = null;
    }

    public long getId() {
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

    public void setRole(UserRole role) {
        this.role = role.name();    
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
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

    public List<OrderItemEntity> getOrders() {
        return orders;
    }
}
