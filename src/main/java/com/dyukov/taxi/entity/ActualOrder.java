package com.dyukov.taxi.entity;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Table(name = "current_status_orders_view")
public class ActualOrder {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private TpOrder order;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private TpUser driver;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TpOrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TpOrder getOrder() {
        return order;
    }

    public void setOrder(TpOrder order) {
        this.order = order;
    }

    public TpUser getDriver() {
        return driver;
    }

    public void setDriver(TpUser driver) {
        this.driver = driver;
    }

    public TpOrderStatus getStatus() {
        return status;
    }

    public void setStatus(TpOrderStatus status) {
        this.status = status;
    }
}
