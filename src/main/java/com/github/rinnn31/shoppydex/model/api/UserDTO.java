package com.github.rinnn31.shoppydex.model.api;

import com.github.rinnn31.shoppydex.model.User;

public class UserDTO {
    private String username;

    private String email;

    private int points;

    private boolean isVerified;

    public UserDTO() {
    }

    public UserDTO(String username, String email, int points, boolean isVerified) {
        this.username = username;
        this.email = email;
        this.points = points;
        this.isVerified = isVerified;
    }

    public UserDTO(User user) {
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

    public int getPoints() {
        return points;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
