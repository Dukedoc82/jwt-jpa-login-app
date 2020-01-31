package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;

import java.util.Collection;
import java.util.List;

public interface IOrderRepository {

    Collection getAll();

    Collection getAllUserOrders(Long retrieverUserId);

    OrderHistory getById(Long id, Long retrieverUserId);

    OrderHistory getById(Long id);

    OrderHistory createOrder(TpOrder order, TpUser updater);

    OrderHistory assignOrderToDriver(OrderHistory order, TpUser driver, TpUser updater);

    Collection<OrderHistory> assignOrdersToDriver(List<Long> orderIds, TpUser driver, TpUser updater);

    OrderHistory cancelOrder(OrderHistory orderDetails);

    OrderHistory completeOrder(OrderHistory orderDetails);

    OrderHistory refuseOrder(OrderHistory orderDetails);

    Collection<OrderHistory> refuseOrders (List<Long> orderIds, TpUser updater);

    Collection getDriverOrders(Long driverId);

    Collection getAssignedDriverOrders(Long driverId);

    Collection getCompletedDriverOrders(Long driverId);

    Collection getCancelledDriverOrders(Long driverId);

    Collection getOpenedUserOrders(Long userId);

    Collection getAssignedUserOrders(Long userId);

    Collection getCancelledUserOrders(Long userId);

    Collection getCompletedUserOrders(Long userId);

    Collection getOpenedOrders();
}
