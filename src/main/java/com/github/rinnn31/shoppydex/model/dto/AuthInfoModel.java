package com.github.rinnn31.shoppydex.model.dto;

public class AuthInfoModel {
    private final String token;

    private final String username;

    private final Long expiredAt;

    public AuthInfoModel(String token, String username, Long expiredAt) {
        this.token = token;
        this.username = username;
        this.expiredAt = expiredAt;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }
}
