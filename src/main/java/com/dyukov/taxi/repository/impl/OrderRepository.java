package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.entity.OrderDetails;
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
public class OrderRepository implements IOrderRepository {

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
            String sql = "Select e from " + OrderDetails.class.getName() + " e";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @NonNull
    public Collection getAllUserOrders(Long retrieverUserId) {
        try {
            String sql = "Select e from " + OrderDetails.class.getName() + " e " +
                    "where e.order.client.userId = :userId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", retrieverUserId);
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Nullable
    public OrderDetails getById(Long id, Long retrieverUserId) {
        try {
            String sql = "Select e from " + OrderDetails.class.getName() + " e " +
                    "where e.order.id = :orderId and (e.order.client.userId = :userId or e.driver.userId = :userId)";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            query.setParameter("userId", retrieverUserId);
            return (OrderDetails) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Nullable
    public OrderDetails getById(Long id) {
        try {
            String sql = "Select e from " + OrderDetails.class.getName() + " e " +
                    "where e.order.id = :orderId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            return (OrderDetails) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public OrderDetails createOrder(TpOrder order, Long retrieverUserId) {
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

    public OrderDetails assignOrderToDriver(Long orderId, Long driverId, Long retrieverId) {
        OrderDetails orderDetails = getById(orderId);
        orderDetails.setDriver(userDetailsRepository.findUserAccount(driverId));
        orderDetails.setStatus(orderStatusRepository.getStatusByKey(OrderStatuses.ASSIGNED));
        entityManager.persist(orderDetails);
        entityManager.flush();
        return orderDetails;
    }

    public OrderDetails cancelOrder(OrderDetails orderDetails) {
        return updateOrderStatus(orderDetails, OrderStatuses.CANCELED);
    }

    public OrderDetails completeOrder(OrderDetails orderDetails) {
        return updateOrderStatus(orderDetails, OrderStatuses.COMPLETED);
    }

    public OrderDetails refuseOrder(OrderDetails orderDetails) {
        orderDetails.setDriver(null);
        return updateOrderStatus(orderDetails, OrderStatuses.OPENED);
    }

    private OrderDetails updateOrderStatus(OrderDetails orderDetails, String newStatus) {
        orderDetails.setStatus(orderStatusRepository.getStatusByKey(newStatus));
        entityManager.persist(orderDetails);
        entityManager.flush();
        return orderDetails;
    }
}
