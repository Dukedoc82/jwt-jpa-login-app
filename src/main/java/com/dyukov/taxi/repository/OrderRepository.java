package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ActualOrder;
import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

@Repository
@Transactional
public class OrderRepository {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private ActualOrderRepository actualOrderRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public TpOrder createOrder(TpOrder order, Long retrieverUserId) {
        entityManager.persist(order);
        entityManager.flush();
        OrderHistory orderHistory = updateOrderHistory(order, retrieverUserId, "tp.status.opened");
        orderHistoryRepository.createOrder(orderHistory);
        return order;
    }

    public ActualOrder assignOrderToDriver(TpOrder order, Long driverId, Long retrieverUserId) {
        OrderHistory orderHistory = updateOrderHistory(order, retrieverUserId, "tp.status.assigned");
        TpUser driver = userDetailsRepository.findUserAccount(driverId);
        orderHistory.setDriver(driver);
        orderHistory.setUpdatedBy(driver);
        orderHistoryRepository.createOrder(orderHistory);
        return actualOrderRepository.getById(order.getId());
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

    public TpOrder getOrderById(Long orderId) {
        try {
            String sql = "Select e from " + TpOrder.class.getName() + " e " //
                    + " Where e.id = :orderId";

            Query query = entityManager.createQuery(sql, TpOrder.class);
            query.setParameter("orderId", orderId);

            return (TpOrder) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private OrderHistory updateOrderHistory(TpOrder order, Long retrieverUserId, String statusKey) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setDate(new Date());
        orderHistory.setOrderStatus(orderStatusRepository.getStatusByKey(statusKey));
        orderHistory.setUpdatedBy(userDetailsRepository.findUserAccount(retrieverUserId));
        return orderHistory;
    }
}
