package com.dyukov.taxi.dao;

public class MailSettingsDao {

    private Long id;

    private UserDao user;

    private Boolean getNewOrderNotifications;

    private Boolean getCancelOrderNotifications;

    private Boolean getCompleteOrderNotifications;

    private Boolean getRefuseOrderNotifications;

    private Boolean getAssignOrderNotifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
        this.user = user;
    }

    public Boolean getGetNewOrderNotifications() {
        return getNewOrderNotifications;
    }

    public void setGetNewOrderNotifications(Boolean getNewOrderNotifications) {
        this.getNewOrderNotifications = getNewOrderNotifications;
    }

    public Boolean getGetCancelOrderNotifications() {
        return getCancelOrderNotifications;
    }

    public void setGetCancelOrderNotifications(Boolean getCancelOrderNotifications) {
        this.getCancelOrderNotifications = getCancelOrderNotifications;
    }

    public Boolean getGetCompleteOrderNotifications() {
        return getCompleteOrderNotifications;
    }

    public void setGetCompleteOrderNotifications(Boolean getCompleteOrderNotifications) {
        this.getCompleteOrderNotifications = getCompleteOrderNotifications;
    }

    public Boolean getGetRefuseOrderNotifications() {
        return getRefuseOrderNotifications;
    }

    public void setGetRefuseOrderNotifications(Boolean getRefuseOrderNotifications) {
        this.getRefuseOrderNotifications = getRefuseOrderNotifications;
    }

    public Boolean getGetAssignOrderNotifications() {
        return getAssignOrderNotifications;
    }

    public void setGetAssignOrderNotifications(Boolean getAssignOrderNotifications) {
        this.getAssignOrderNotifications = getAssignOrderNotifications;
    }
}
