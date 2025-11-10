package com.github.rinnn31.shoppydex.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordModel {
    @NotBlank(message = "Token không được để trống")
    private String code;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, message = "Mật khẩu mới phải có ít nhất 8 ký tự")
    private String newPassword;

    public ResetPasswordModel(String username, String code, String newPassword) {
        this.code = code;
        this.username = username;
        this.newPassword = newPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String token) {
        this.code = token;
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