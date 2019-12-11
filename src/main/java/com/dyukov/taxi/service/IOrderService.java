package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.dao.OrderDao;

import java.util.Collection;

public interface IOrderService {

    OrderDetailsDao createOrder(OrderDao orderDao, Long updatedBy);

    OrderDetailsDao getOrderById(Long id, Long retrieverUserId);

    OrderDetailsDao getOrderById(Long id);

    Collection<OrderDetailsDao> getActualOrders();

    OrderDetailsDao assignOrderToDriver(OrderDetailsDao orderDao, Long driverId, Long updatedBy);

    OrderDetailsDao cancelOrder(Long orderId, Long retrieverUserId);

    OrderDetailsDao completeOrder(Long orderId, Long driverId);

    OrderDetailsDao refuseOrder(Long id, Long driverId);

    Collection<OrderDetailsDao> getActualUserOrders(Long retrieverUserId);
}
