package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.OrderDao;

import java.util.Collection;

public interface IOrderService {

    ActualOrderDao createOrder(OrderDao orderDao, Long updatedBy);

    ActualOrderDao getOrderById(Long id, Long retrieverUserId);

    ActualOrderDao getOrderById(Long id);

    Collection<ActualOrderDao> getActualOrders();

    ActualOrderDao assignOrderToDriver(ActualOrderDao orderDao, Long driverId, Long updatedBy);

    ActualOrderDao cancelOrder(Long orderId, Long retrieverUserId);

    ActualOrderDao completeOrder(Long orderId, Long driverId);

    ActualOrderDao refuseOrder(Long id, Long driverId);

    Collection<ActualOrderDao> getActualUserOrders(Long retrieverUserId);
}
