package com.dyukov.taxi.config;

public enum UserRoles {
    ROLE_ADMIN("role_admin"),
    ROLE_USER("role_user"),
    ROLE_DRIVER("role_driver");

    private String roleTitle;

    private UserRoles(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getRoleTitle() {
        return roleTitle;
    }
}
