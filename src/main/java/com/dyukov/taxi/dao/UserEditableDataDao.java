package com.dyukov.taxi.dao;

import com.dyukov.taxi.entity.TpRole;

public class UserEditableDataDao {

    private Long userId;
    private String firstName;
    private String lastName;
    private TpRole role;

    public UserEditableDataDao() {
        super();
    }

    public UserEditableDataDao(Long userId, String firstName, String lastName, TpRole role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public TpRole getRole() {
        return role;
    }

    public void setRole(TpRole role) {
        this.role = role;
    }
}
