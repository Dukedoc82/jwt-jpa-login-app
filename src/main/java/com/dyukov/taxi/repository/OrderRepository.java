package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class OrderRepository {

    @Autowired
    private EntityManager entityManager;

    public TpOrder createOrder(TpOrder order) {
        entityManager.persist(order);
        entityManager.flush();
        return order;
    }

    public TpOrder getOrderById(Long orderId, Long retrieverUserId) {
        try {
            String sql = "Select e from " + TpOrder.class.getName() + " e " //
                    + " Where e.id = :orderId AND e.client.userId = :userId";

            Query query = entityManager.createQuery(sql, TpOrder.class);
            query.setParameter("orderId", orderId);
            query.setParameter("userId", retrieverUserId);

            return (TpOrder) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
