package com.dyukov.taxi.dao;

public class UserRolesDao {

    private boolean isDriver;
    private boolean isAdmin;

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
