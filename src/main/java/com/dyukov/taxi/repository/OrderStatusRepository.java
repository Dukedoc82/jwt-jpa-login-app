package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderStatusRepository {

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

    public List getAvailableStatuses() {
        try {
            String sql = "Select e from " + TpOrderStatus.class.getName() + " e";

            Query query = entityManager.createQuery(sql, TpOrderStatus.class);

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }
}
