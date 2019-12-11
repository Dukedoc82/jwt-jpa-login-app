package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.OrderDetails;
import com.dyukov.taxi.entity.TpOrder;

import java.util.Collection;

public interface IOrderRepository {

    Collection getAll();

    Collection getAllUserOrders(Long retrieverUserId);

    OrderDetails getById(Long id, Long retrieverUserId);

    OrderDetails getById(Long id);

    OrderDetails createOrder(TpOrder order, Long retrieverUserId);

    OrderDetails assignOrderToDriver(Long orderId, Long driverId, Long retrieverId);

    OrderDetails cancelOrder(OrderDetails orderDetails);

    OrderDetails completeOrder(OrderDetails orderDetails);

    OrderDetails refuseOrder(OrderDetails orderDetails);
}
