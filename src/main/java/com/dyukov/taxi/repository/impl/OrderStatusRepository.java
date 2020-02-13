package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.exception.StatusNotFoundException;
import com.dyukov.taxi.exception.TaxiServiceException;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;

@Repository
public class OrderStatusRepository implements IOrderStatusRepository {

    private static final String NO_STATUS_FOUND_MESSAGE = "No status found.";
    private Logger logger = LoggerFactory.getLogger(OrderStatusRepository.class);

    @Autowired
    private EntityManager entityManager;

    @Cacheable("statuses")
    public TpOrderStatus getStatusByKey(String key) {
        try {
            String sql = "Select e from " + TpOrderStatus.class.getName() + " e "
                    + " Where e.titleKey = :titleKey ";

            Query query = entityManager.createQuery(sql, TpOrderStatus.class);
            query.setParameter("titleKey", key);

            return (TpOrderStatus) query.getSingleResult();
        } catch (NoResultException e) {
            String message = String.format(TaxiServiceException.STATUS_DOES_NOT_EXIST, key);
            logger.error(message, e);
            throw new StatusNotFoundException(message, e);
        }
    }

    @Override
    public TpOrderStatus getStatusById(Long id) {
        try {
            String sql = "select os from " + TpOrderStatus.class.getName() + " os " +
                    "where os.id = :statusId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("statusId", id);
            return (TpOrderStatus) query.getSingleResult();
        } catch (NoResultException e) {
            String message = String.format(TaxiServiceException.STATUS_DOES_NOT_EXIST, id);
            logger.error(message, e);
            throw new StatusNotFoundException(message, e);
        }
    }

    public Collection getAvailableStatuses() {
        try {
            String sql = "Select e from " + TpOrderStatus.class.getName() + " e";

            Query query = entityManager.createQuery(sql, TpOrderStatus.class);

            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_STATUS_FOUND_MESSAGE);
            return new ArrayList();
        }

    }
}
