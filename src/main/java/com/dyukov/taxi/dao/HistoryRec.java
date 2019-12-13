package com.dyukov.taxi.dao;

import java.util.Date;

public class HistoryRec {

    private OrderDao order;

    private Status orderStatus;

    private UserDao driver;

    private UserDao updatedBy;

    private Date updatedOn;

    public OrderDao getOrder() {
        return order;
    }

    public void setOrder(OrderDao order) {
        this.order = order;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public UserDao getDriver() {
        return driver;
    }

    public void setDriver(UserDao driver) {
        this.driver = driver;
    }

    public UserDao getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UserDao updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
