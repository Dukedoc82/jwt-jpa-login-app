package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.OrderDao;

import java.util.Collection;

public interface IOrderService {

    HistoryRec createOrder(OrderDao orderDao, Long updatedBy);

    HistoryRec getOrderById(Long id, Long retrieverUserId);

    Collection getActualOrders();

    HistoryRec assignOrderToDriver(Long orderId, Long driverId, Long updatedBy);

    HistoryRec cancelOrder(Long orderId, Long retrieverUserId);

    HistoryRec completeOrder(Long orderId, Long driverId);

    HistoryRec refuseOrder(Long id, Long updaterId);

    Collection getActualUserOrders(Long retrieverUserId);

    Collection getActualUserOrders(Long userId, Long retrieverUserId);

    Collection getDriverOrders(Long driverId);

    Collection getAssignedDriverOrders(Long driverId);

    Collection getCompletedDriverOrders(Long driverId);

    Collection getCancelledDriverOrders(Long driverId);

    Collection getOpenedUserOrders(Long userId);

    Collection getAssignedUserOrders(Long userId);

    Collection getCancelledUserOrders(Long userId);
}
