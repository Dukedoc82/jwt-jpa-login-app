package com.dyukov.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "TP_User", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "TP_USER_UK", columnNames = "User_Name") })
public class TpUser implements Serializable {

    private static final long serialVersionUID = 3007298680791369536L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp_user_seq")
    @SequenceGenerator(name = "tp_user_seq", sequenceName = "tp_user_seq", schema = "dbo", allocationSize = 1)
    @Column(name = "User_Id")
    private Long userId;

    @Column(name = "User_Name", length = 36, nullable = false)
    private String userName;

    @Column(name = "First_Name", length = 128, nullable = false)
    private String firstName;

    @Column(name = "Last_Name", length = 128, nullable = false)
    private String lastName;

    @Column(name = "Phone_Number", length = 11, nullable = false)
    private String phoneNumber;

    @Column(name = "Encryted_Password", length = 128, nullable = false)
    @JsonIgnore
    private String encrytedPassword;

    @Column(name = "Enabled", length = 1, nullable = false)
    @JsonIgnore
    private boolean enabled;

    @Transient
    @JsonIgnore
    private Collection roleNames;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncrytedPassword() {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Collection getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(Collection roleNames) {
        this.roleNames = roleNames;
    }

    @Override
    public String toString() {
        return "TpUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
