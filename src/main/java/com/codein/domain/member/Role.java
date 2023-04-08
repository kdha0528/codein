package com.codein.domain.member;

public enum Role {

    NONE("NONE"),
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
