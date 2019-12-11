package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.entity.ActualOrder;
import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.repository.IOrderHistoryRepository;
import com.dyukov.taxi.repository.IOrderRepository;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import com.dyukov.taxi.repository.IUserDetailsRepository;
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
import java.util.Date;

@Repository
@Transactional
public class ActualOrderRepository implements IOrderRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    private IOrderHistoryRepository orderHistoryRepository;

    @NonNull
    public Collection getAll() {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @NonNull
    public Collection getAllUserOrders(Long retrieverUserId) {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e " +
                    "where e.order.client.userId = :userId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", retrieverUserId);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Nullable
    public ActualOrder getById(Long id, Long retrieverUserId) {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e " +
                    "where e.order.id = :orderId and (e.order.client.userId = :userId or e.driver.userId = :userId)";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            query.setParameter("userId", retrieverUserId);
            return (ActualOrder) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
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

    public ActualOrder createOrder(TpOrder order, Long retrieverUserId) {
        entityManager.persist(order);
        entityManager.flush();
        OrderHistory orderHistory = updateOrderHistory(order, retrieverUserId, "tp.status.opened");
        orderHistoryRepository.createOrder(orderHistory);
        return getById(order.getId());
    }

    private OrderHistory updateOrderHistory(TpOrder order, Long retrieverUserId, String statusKey) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setDate(new Date());
        orderHistory.setOrderStatus(orderStatusRepository.getStatusByKey(statusKey));
        orderHistory.setUpdatedBy(userDetailsRepository.findUserAccount(retrieverUserId));
        return orderHistory;
    }

    public ActualOrder assignOrderToDriver(Long orderId, Long driverId, Long retrieverId) {
        ActualOrder actualOrder = getById(orderId);
        actualOrder.setDriver(userDetailsRepository.findUserAccount(driverId));
        actualOrder.setStatus(orderStatusRepository.getStatusByKey(OrderStatuses.ASSIGNED));
        entityManager.persist(actualOrder);
        entityManager.flush();
        return actualOrder;
    }

    public ActualOrder cancelOrder(ActualOrder actualOrder) {
        return updateOrderStatus(actualOrder, OrderStatuses.CANCELED);
    }

    public ActualOrder completeOrder(ActualOrder actualOrder) {
        return updateOrderStatus(actualOrder, OrderStatuses.COMPLETED);
    }

    public ActualOrder refuseOrder(ActualOrder actualOrder) {
        actualOrder.setDriver(null);
        return updateOrderStatus(actualOrder, OrderStatuses.OPENED);
    }

    private ActualOrder updateOrderStatus(ActualOrder actualOrder, String newStatus) {
        actualOrder.setStatus(orderStatusRepository.getStatusByKey(newStatus));
        entityManager.persist(actualOrder);
        entityManager.flush();
        return actualOrder;
    }
}
