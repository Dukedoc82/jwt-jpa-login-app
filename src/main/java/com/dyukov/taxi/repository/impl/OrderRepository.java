package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.entity.OrderDetails;
import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.exception.OrderNotFoundException;
import com.dyukov.taxi.repository.IOrderHistoryRepository;
import com.dyukov.taxi.repository.IOrderRepository;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;

@Repository
@Transactional
public class OrderRepository implements IOrderRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    @Autowired
    private IOrderHistoryRepository orderHistoryRepository;

    @NonNull
    public Collection getAll() {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where e.order.id = r.order.id)";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @NonNull
    public Collection getAllUserOrders(Long retrieverUserId) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.order.client.userId = :userId and " +
                    "e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where e.order.id = r.order.id)";
            /*String sql = "Select e from " + OrderDetails.class.getName() + " e " +
                    "where e.order.client.userId = :userId";*/
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", retrieverUserId);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Nullable
    public OrderHistory getById(Long id, Long retrieverUserId, boolean isDriver) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.order.id = :orderId " +
                    "and e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where r.order.id = :orderId)";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            query.setMaxResults(1);
            OrderHistory result = (OrderHistory) query.getSingleResult();
            if (isDriver || retrieverUserId.equals(result.getOrder().getClient().getUserId())) {
                return result;
            } else {
                throw new OrderNotFoundException(id);
            }
        } catch (NoResultException e) {
            throw new OrderNotFoundException(id, e);
        }
    }

    @Nullable
    public OrderHistory getById(Long id) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.order.id = :orderId " +
                    "order by e.updatedOn desc";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            query.setMaxResults(1);
            return (OrderHistory) query.getSingleResult();
        } catch (NoResultException e) {
            throw new OrderNotFoundException(id, e);
        }
    }

    public OrderHistory createOrder(TpOrder order, TpUser updater) {
        entityManager.persist(order);
        entityManager.flush();
        OrderHistory orderHistory = new OrderHistory(order,
                orderStatusRepository.getStatusByKey(OrderStatuses.OPENED),
                null,
                updater);
        return orderHistoryRepository.createOrder(orderHistory);
    }

    public OrderHistory assignOrderToDriver(OrderHistory orderHistory, TpUser driver, TpUser updater) {
        return orderHistoryRepository.createOrder(new OrderHistory(orderHistory.getOrder(),
                orderStatusRepository.getStatusByKey(OrderStatuses.ASSIGNED),
                driver,
                updater));
    }

    public OrderHistory cancelOrder(OrderHistory orderHistory) {
        return updateOrderStatus(orderHistory, OrderStatuses.CANCELED);
    }

    public OrderHistory completeOrder(OrderHistory orderHistory) {
        return updateOrderStatus(orderHistory, OrderStatuses.COMPLETED);
    }

    public OrderHistory refuseOrder(OrderHistory orderDetails, TpUser updatedBy) {
        OrderHistory orderHistory = new OrderHistory(orderDetails.getOrder(),
                orderStatusRepository.getStatusByKey(OrderStatuses.OPENED),
                null, updatedBy);
        entityManager.persist(orderHistory);
        entityManager.flush();
        return orderHistory;
    }

    @Override
    public Collection getDriverOrders(Long driverId) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.driver.userId = :driverId and " +
                    "e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where r.driver.userId = e.driver.userId " +
                    "and e.order.id = r.order.id)";
            Query query = entityManager.createQuery(sql);
            query.setParameter("driverId", driverId);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList();
        }
    }

    private OrderHistory updateOrderStatus(OrderHistory orderDetails, String newStatus) {
        orderDetails.setOrderStatus(orderStatusRepository.getStatusByKey(newStatus));
        entityManager.persist(orderDetails);
        entityManager.flush();
        return orderDetails;
    }

}
