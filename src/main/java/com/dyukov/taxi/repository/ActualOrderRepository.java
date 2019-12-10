package com.dyukov.taxi.repository;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.entity.ActualOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ActualOrderRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @NonNull
    public List<ActualOrder> getAll() {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Nullable
    public ActualOrder getById(Long id) {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e " +
                    "where e.order.id = :orderId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            return (ActualOrder) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ActualOrder cancelOrder(ActualOrder actualOrder) {
        actualOrder.setStatus(orderStatusRepository.getStatusByKey(OrderStatuses.CANCELED));
        entityManager.persist(actualOrder);
        entityManager.flush();
        return actualOrder;
    }
}
