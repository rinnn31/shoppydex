package com.github.rinnn31.shoppydex.model.api;

import com.github.rinnn31.shoppydex.model.entity.UserEntity;

public class UserInfoModel {
    private String username;

    private String email;

    private Double points;

    private Boolean isVerified;

    public UserInfoModel() {
    }

    public UserInfoModel(String username, String email, Double points, Boolean isVerified) {
        this.username = username;
        this.email = email;
        this.points = points;
        this.isVerified = isVerified;
    }

    public UserInfoModel(UserEntity user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.points = user.getPoints();
        this.isVerified = user.isVerified();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Double getPoints() {
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

    public void setPoints(Double points) {
        this.points = points;
    }

    public void setVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
