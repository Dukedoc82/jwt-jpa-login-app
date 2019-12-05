package com.dyukov.taxi.entity;

import javax.persistence.*;

@Entity
@Table(name = "Tp_Role", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "Tp_ROLE_UK", columnNames = "Role_Name") })
public class TpRole {

    @Id
    @GeneratedValue
    @Column(name = "Role_Id", nullable = false)
    private Long roleId;

    @Column(name = "Role_Name", length = 30, nullable = false)
    private String roleName;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
