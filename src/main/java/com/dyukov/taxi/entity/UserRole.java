package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User_Role",
        uniqueConstraints = {
                @UniqueConstraint(name = "USER_ROLE_UK", columnNames = { "User_Id", "Role_Id" }) })
public class UserRole implements Serializable {

    private static final long serialVersionUID = -6778863180063349822L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "User_Id", nullable = false)
    private TpUser tpUser;

    @ManyToOne
    @JoinColumn(name = "Role_Id", nullable = false)
    private TpRole tpRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TpUser getTpUser() {
        return tpUser;
    }

    public void setTpUser(TpUser tpUser) {
        this.tpUser = tpUser;
    }

    public TpRole getTpRole() {
        return tpRole;
    }

    public void setTpRole(TpRole tpRole) {
        this.tpRole = tpRole;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", tpUser=" + tpUser +
                ", tpRole=" + tpRole +
                '}';
    }
}
