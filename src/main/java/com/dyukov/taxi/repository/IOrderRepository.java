package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;

import java.util.Collection;

public interface IOrderRepository {

    Collection getAll();

    Collection getAllUserOrders(Long retrieverUserId);

    OrderHistory getById(Long id, Long retrieverUserId, boolean isDriver);

    OrderHistory getById(Long id);

    OrderHistory createOrder(TpOrder order, TpUser updater);

    OrderHistory assignOrderToDriver(OrderHistory order, TpUser driver, TpUser updater);

    OrderHistory cancelOrder(OrderHistory orderDetails);

    OrderHistory completeOrder(OrderHistory orderDetails);

    OrderHistory refuseOrder(OrderHistory orderDetails, TpUser updater);

    Collection getDriverOrders(Long driverId);
}
