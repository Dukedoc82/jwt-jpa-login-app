package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ActualOrder;
import com.dyukov.taxi.entity.TpOrder;

import java.util.Collection;

public interface IOrderRepository {

    Collection getAll();

    Collection getAllUserOrders(Long retrieverUserId);

    ActualOrder getById(Long id, Long retrieverUserId);

    ActualOrder getById(Long id);

    ActualOrder createOrder(TpOrder order, Long retrieverUserId);

    ActualOrder assignOrderToDriver(Long orderId, Long driverId, Long retrieverId);

    ActualOrder cancelOrder(ActualOrder actualOrder);

    ActualOrder completeOrder(ActualOrder actualOrder);

    ActualOrder refuseOrder(ActualOrder actualOrder);
}
