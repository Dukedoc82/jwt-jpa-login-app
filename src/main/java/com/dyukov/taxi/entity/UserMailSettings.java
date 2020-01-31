package com.dyukov.taxi.entity;

import javax.persistence.*;

@Entity
@Table(name = "TP_USER_MAIL_SETTINGS")
public class UserMailSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp_user_mail_settings_seq")
    @SequenceGenerator(name = "tp_user_mail_settings_seq", sequenceName = "tp_user_mail_settings_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TpUser user;

    @Column
    private Boolean newOrder;

    @Column
    private Boolean assignOrder;

    @Column
    private Boolean cancelOrder;

    @Column
    private Boolean completeOrder;

    @Column
    private Boolean refuseOrder;

    public UserMailSettings() {
        this.newOrder = true;
        this.assignOrder = true;
        this.cancelOrder = true;
        this.completeOrder = true;
        this.refuseOrder = true;
    }

    public UserMailSettings(TpUser user) {
        this();
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TpUser getUser() {
        return user;
    }

    public void setUser(TpUser user) {
        this.user = user;
    }

    public Boolean getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Boolean newOrder) {
        this.newOrder = newOrder;
    }

    public Boolean getAssignOrder() {
        return assignOrder;
    }

    public void setAssignOrder(Boolean assignOrder) {
        this.assignOrder = assignOrder;
    }

    public Boolean getCancelOrder() {
        return cancelOrder;
    }

    public void setCancelOrder(Boolean cancelOrder) {
        this.cancelOrder = cancelOrder;
    }

    public Boolean getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(Boolean completeOrder) {
        this.completeOrder = completeOrder;
    }

    public Boolean getRefuseOrder() {
        return refuseOrder;
    }

    public void setRefuseOrder(Boolean refuseOrder) {
        this.refuseOrder = refuseOrder;
    }
}
