package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Tp_Order_History")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_Id")
    private TpOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Status_Id")
    private TpOrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Driver_Id")
    private TpUser driver;

    @Column(name = "Update_Datetime")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Updated_By")
    private TpUser updatedBy;

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

    public TpOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(TpOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public TpUser getDriver() {
        return driver;
    }

    public void setDriver(TpUser driver) {
        this.driver = driver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TpUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(TpUser updatedBy) {
        this.updatedBy = updatedBy;
    }

}
