package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.repository.IOrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class OrderHistoryRepository implements IOrderHistoryRepository {

    @Autowired
    private EntityManager entityManager;

    public OrderHistory createOrder(OrderHistory orderHistory) {
        entityManager.persist(orderHistory);
        entityManager.flush();
        return orderHistory;
    }
}
