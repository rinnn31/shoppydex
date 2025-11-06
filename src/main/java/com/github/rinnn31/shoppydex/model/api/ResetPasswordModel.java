package com.github.rinnn31.shoppydex.model.api;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordModel {
    @NotBlank(message = "Token không được để trống")
    private String token;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    public ResetPasswordModel(String username, String token, String newPassword) {
        this.token = token;
        this.username = username;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}