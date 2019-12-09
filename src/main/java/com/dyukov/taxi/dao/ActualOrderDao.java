package com.dyukov.taxi.dao;

import com.dyukov.taxi.entity.TpOrderStatus;

public class ActualOrderDao {

    private Long id;

    private OrderDao order;

    private UserDao driver;

    private TpOrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderDao getOrder() {
        return order;
    }

    public void setOrder(OrderDao order) {
        this.order = order;
    }

    public UserDao getDriver() {
        return driver;
    }

    public void setDriver(UserDao driver) {
        this.driver = driver;
    }

    public TpOrderStatus getStatus() {
        return status;
    }

    public void setStatus(TpOrderStatus status) {
        this.status = status;
    }
}
