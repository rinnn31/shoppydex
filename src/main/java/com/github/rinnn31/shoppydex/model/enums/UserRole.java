package com.github.rinnn31.shoppydex.model.enums;

public enum UserRole {
    USER,
    ADMIN;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
