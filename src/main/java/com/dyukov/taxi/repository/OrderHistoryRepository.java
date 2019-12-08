package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.OrderHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class OrderHistoryRepository {

    @Autowired
    private EntityManager entityManager;

    public OrderHistory createOrder(OrderHistory orderHistory) {
        entityManager.persist(orderHistory);
        entityManager.flush();
        return orderHistory;
    }
}
