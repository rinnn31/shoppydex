package com.github.rinnn31.shoppydex.model.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangeEmailModel {
    @NotBlank(message = "Email mới không được để trống")
    private String newEmail;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    public ChangeEmailModel() {
    }

    public ChangeEmailModel(String newEmail, String password) {
        this.newEmail = newEmail;
        this.password = password;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
