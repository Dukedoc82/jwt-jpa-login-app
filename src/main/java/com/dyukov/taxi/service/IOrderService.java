package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.dao.OrderDao;

import java.util.Collection;

public interface IOrderService {

    HistoryRec createOrder(OrderDao orderDao, Long updatedBy);

    HistoryRec getOrderById(Long id, Long retrieverUserId);

    HistoryRec getOrderById(Long id);

    Collection getActualOrders();

    HistoryRec assignOrderToDriver(Long orderId, Long driverId, Long updatedBy);

    HistoryRec cancelOrder(Long orderId, Long retrieverUserId);

    HistoryRec completeOrder(Long orderId, Long driverId);

    HistoryRec completeOrderAsAdmin(Long orderId);

    HistoryRec refuseOrder(Long id, Long driverId);

    Collection getActualUserOrders(Long retrieverUserId);

    Collection getDriverOrders(Long driverId);

    Collection getAssignedDriverOrders(Long driverId);
}
