package com.dyukov.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Immutable;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Immutable
@Table(name = "current_status_orders_view")
public class OrderDetails {

    @Id
    @JsonIgnore
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hist_id")
    private OrderHistory historyRec;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
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

    public OrderHistory getHistoryRec() {
        return historyRec;
    }

    public void setHistoryRec(OrderHistory historyRec) {
        this.historyRec = historyRec;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", order=" + order +
                ", driver=" + driver +
                ", status=" + status +
                ", historyRec=" + historyRec +
                '}';
    }
}
