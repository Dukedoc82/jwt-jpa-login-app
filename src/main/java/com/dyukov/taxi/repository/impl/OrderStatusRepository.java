package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collection;

@Repository
public class OrderStatusRepository implements IOrderStatusRepository {

    @Autowired
    private EntityManager entityManager;

    public TpOrderStatus getStatusByKey(String key) {
        try {
            String sql = "Select e from " + TpOrderStatus.class.getName() + " e "
                    + " Where e.titleKey = :titleKey ";

            Query query = entityManager.createQuery(sql, TpOrderStatus.class);
            query.setParameter("titleKey", key);

            return (TpOrderStatus) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection getAvailableStatuses() {
        try {
            String sql = "Select e from " + TpOrderStatus.class.getName() + " e";

            Query query = entityManager.createQuery(sql, TpOrderStatus.class);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }
}
