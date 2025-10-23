package com.github.rinnn31.shoppydex.model.api;

public class AuthenticationDTO {
    private final String token;

    private final String username;

    private final Long expiredAt;

    public AuthenticationDTO(String token, String username, Long expiredAt) {
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
