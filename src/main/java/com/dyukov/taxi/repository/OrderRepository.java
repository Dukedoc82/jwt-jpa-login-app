package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ActualOrder;
import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
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

    public ActualOrder createOrder(TpOrder order, Long retrieverUserId) {
        entityManager.persist(order);
        entityManager.flush();
        OrderHistory orderHistory = updateOrderHistory(order, retrieverUserId, "tp.status.opened");
        orderHistoryRepository.createOrder(orderHistory);
        return actualOrderRepository.getById(order.getId());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ActualOrder assignOrderToDriver(TpOrder order, Long driverId, Long retrieverUserId) {
        updateOrderAssignDriver(order, driverId, retrieverUserId);
        return actualOrderRepository.getById(order.getId());
    }

    private TpOrder updateOrderAssignDriver(TpOrder order, Long driverId, Long retrieverUserId) {
        OrderHistory orderHistory = updateOrderHistory(order, retrieverUserId, "tp.status.assigned");
        TpUser driver = userDetailsRepository.findUserAccount(driverId);
        orderHistory.setDriver(driver);
        orderHistory.setUpdatedBy(driver);
        orderHistoryRepository.createOrder(orderHistory);
        return order;
    }

    public ActualOrder getOrderById(Long orderId, Long retrieverUserId) {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e " //
                    + " Where e.id = :orderId AND (e.order.client.userId = :userId OR e.driver.userId = :userId)";

            Query query = entityManager.createQuery(sql, ActualOrder.class);
            query.setParameter("orderId", orderId);
            query.setParameter("userId", retrieverUserId);

            return (ActualOrder) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ActualOrder getOrderById(Long orderId) {
        try {
            String sql = "Select e from " + ActualOrder.class.getName() + " e " //
                    + " Where e.id = :orderId";

            Query query = entityManager.createQuery(sql, ActualOrder.class);
            query.setParameter("orderId", orderId);

            return (ActualOrder) query.getSingleResult();
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
