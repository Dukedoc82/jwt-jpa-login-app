package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.OrderHistory;

public interface IOrderHistoryRepository {

    OrderHistory createOrder(OrderHistory orderHistory);

}
