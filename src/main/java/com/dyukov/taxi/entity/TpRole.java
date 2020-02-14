package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Tp_Role", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "Tp_ROLE_UK", columnNames = "Role_Name") })
public class TpRole implements Serializable {

    private static final long serialVersionUID = 5777871383733820000L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp_role_seq")
    @SequenceGenerator(name = "tp_role_seq", sequenceName = "tp_role_seq", allocationSize = 1)
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

    @Override
    public String toString() {
        return "TpRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
