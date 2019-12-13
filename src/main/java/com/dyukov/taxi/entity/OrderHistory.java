package com.dyukov.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Tp_Order_History")
public class OrderHistory implements Serializable {

    private static final long serialVersionUID = 8681789090807370488L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Order_Id")
    private TpOrder order;

    @ManyToOne
    @JoinColumn(name = "Status_Id")
    private TpOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "Driver_Id")
    private TpUser driver;

    @Column(name = "Update_Datetime")
    private Date updatedOn;

    @ManyToOne
    @JoinColumn(name = "Updated_By")
    private TpUser updatedBy;

    public OrderHistory() {
        super();
    }

    private OrderHistory(TpOrder order, TpOrderStatus status, TpUser driver) {
        this.order = order;
        this.status = status;
        this.driver = driver;
        this.updatedBy = driver;
        this.updatedOn = new Date();
    }

    public OrderHistory(TpOrder order, TpOrderStatus orderStatus, TpUser driver, TpUser updatedBy) {
        this(order, orderStatus, driver);
        this.updatedBy = updatedBy;
    }

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

    public TpOrderStatus getStatus() {
        return status;
    }

    public void setOrderStatus(TpOrderStatus status) {
        this.status = status;
    }

    public TpUser getDriver() {
        return driver;
    }

    public void setDriver(TpUser driver) {
        this.driver = driver;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public TpUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(TpUser updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "id=" + id +
                ", order=" + order +
                ", status=" + status +
                ", driver=" + driver +
                ", updatedOn=" + updatedOn +
                ", updatedBy=" + updatedBy +
                '}';
    }
}
