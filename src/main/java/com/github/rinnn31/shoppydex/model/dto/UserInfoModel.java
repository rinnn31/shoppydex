package com.github.rinnn31.shoppydex.model.dto;

import com.github.rinnn31.shoppydex.model.entity.UserEntity;
import com.github.rinnn31.shoppydex.model.enums.UserRole;

public class UserInfoModel {
    private String username;

    private String email;

    private Integer points;

    private Boolean isVerified;

    private Boolean isAdmin;

    public UserInfoModel() {
    }

    public UserInfoModel(String username, String email, Integer points, Boolean isVerified, Boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.points = points;
        this.isVerified = isVerified;
        this.isAdmin = isAdmin;
    }

    public UserInfoModel(UserEntity user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.points = user.getPoints();
        this.isAdmin = user.getRole().equals(UserRole.ADMIN.name());
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPoints() {
        return points;
    }

    public Boolean isVerified() {
        return isVerified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
