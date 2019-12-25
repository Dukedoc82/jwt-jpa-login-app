package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.dao.OrderDao;

import java.util.Collection;
import java.util.List;

public interface IOrderService {

    OrderDetailsDao createOrder(OrderDao orderDao, Long updatedBy);

    OrderDetailsDao getOrderById(Long id, Long retrieverUserId);

    Collection getActualOrders();

    OrderDetailsDao assignOrderToDriver(Long orderId, Long driverId, Long updatedBy);

    OrderDetailsDao cancelOrder(Long orderId, Long retrieverUserId);

    OrderDetailsDao completeOrder(Long orderId, Long driverId);

    OrderDetailsDao refuseOrder(Long id, Long updaterId);

    Collection getActualUserOrders(Long retrieverUserId);

    Collection getActualUserOrders(Long userId, Long retrieverUserId);

    Collection getDriverOrders(Long driverId);

    Collection getAssignedDriverOrders(Long driverId);

    Collection getCompletedDriverOrders(Long driverId);

    Collection getCancelledDriverOrders(Long driverId);

    Collection getOpenedUserOrders(Long userId);

    Collection getAssignedUserOrders(Long userId);

    Collection getCancelledUserOrders(Long userId);

    Collection getCompletedUserOrders(Long userId);

    Collection getOpenedOrders();

    Collection refuseOrders(List<Long> orderIds, Long updaterId);

    Collection assignOrdersToDriver(List<Long> orderIds, Long updaterId);

    Collection completeOrders(List<Long> orderIds, Long updaterId);
}
