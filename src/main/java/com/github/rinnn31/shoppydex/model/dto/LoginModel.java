package com.github.rinnn31.shoppydex.model.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginModel {
    @NotBlank(message = "Username/Email không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
