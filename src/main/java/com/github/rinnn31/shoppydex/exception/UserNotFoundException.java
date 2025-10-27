package com.github.rinnn31.shoppydex.exception;

public class UserNotFoundException extends SPDException {
    private final String username;

    public UserNotFoundException(String username) {
        super(101, "Người dùng '" + username + "' không tồn tại");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
