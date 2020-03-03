package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.dao.UpdateOrderDao;
import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.exception.OrderNotFoundException;
import com.dyukov.taxi.exception.TaxiServiceException;
import com.dyukov.taxi.repository.IOrderHistoryRepository;
import com.dyukov.taxi.repository.IOrderRepository;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional
public class OrderRepository implements IOrderRepository {

    private static final String NO_ORDERS_FOUND_MESSAGE = "No orders found";

    private Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    @Autowired
    private IOrderHistoryRepository orderHistoryRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @NonNull
    public Collection getAll() {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where e.order.id = r.order.id)";
            Query query = entityManager.createQuery(sql);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_ORDERS_FOUND_MESSAGE);
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
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", retrieverUserId);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_ORDERS_FOUND_MESSAGE);
            return new ArrayList<>();
        }
    }

    @Nullable
    public OrderHistory getById(Long id, Long retrieverUserId) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.order.id = :orderId " +
                    "and e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where r.order.id = :orderId)";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderId", id);
            query.setMaxResults(1);
            return (OrderHistory) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(String.format(TaxiServiceException.ORDER_DOES_NOT_EXIST, id), e);
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
            logger.error(String.format(TaxiServiceException.ORDER_DOES_NOT_EXIST, id), e);
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

    @Override
    public Collection<OrderHistory> assignOrdersToDriver(List<Long> orderIds, TpUser driver, TpUser updater) {
        List<OrderHistory> orders = getByIds(orderIds);
        TpOrderStatus assignedStatus = orderStatusRepository.getStatusByKey(OrderStatuses.ASSIGNED);
        List<OrderHistory> updatedOrders = new ArrayList<>();
        orders.forEach(orderHistory -> {
            OrderHistory updateOrder = new OrderHistory(orderHistory);
            updateOrder.setOrderStatus(assignedStatus);
            updateOrder.setDriver(driver);
            updateOrder.setUpdatedBy(updater);
            entityManager.persist(updateOrder);
            entityManager.flush();
            updatedOrders.add(updateOrder);
        });
        return updatedOrders;
    }

    public OrderHistory cancelOrder(OrderHistory orderHistory) {
        return updateOrderStatus(orderHistory, OrderStatuses.CANCELED);
    }

    public OrderHistory completeOrder(OrderHistory orderHistory) {
        return updateOrderStatus(orderHistory, OrderStatuses.COMPLETED);
    }

    public OrderHistory refuseOrder(OrderHistory orderDetails) {
        orderDetails.setDriver(null);
        return updateOrderStatus(orderDetails, OrderStatuses.OPENED);
    }

    @Override
    public Collection<OrderHistory> refuseOrders(List<Long> orderIds, TpUser updater) {
        List<OrderHistory> orders = getByIds(orderIds).stream()
                .filter(orderHistory -> orderHistory.getDriver() != null && orderHistory.getDriver().getUserId().equals(updater.getUserId()))
                .collect(Collectors.toList());
        List<OrderHistory> updateOrders = new ArrayList<>();
        orders.forEach(orderHistory -> {
            OrderHistory updateOrder = new OrderHistory(orderHistory);
            updateOrder.setOrderStatus(orderStatusRepository.getStatusByKey(OrderStatuses.OPENED));
            updateOrder.setUpdatedBy(updater);
            updateOrder.setDriver(null);
            entityManager.persist(updateOrder);
            updateOrders.add(updateOrder);
        });
        entityManager.flush();
        return updateOrders;
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
            logger.error(NO_ORDERS_FOUND_MESSAGE);
            return new ArrayList();
        }
    }

    @Override
    public Collection getAssignedDriverOrders(Long driverId) {
        return getDriverOrdersByStatus(driverId, OrderStatuses.ASSIGNED);
    }

    @Override
    public Collection getCompletedDriverOrders(Long driverId) {
        return getDriverOrdersByStatus(driverId, OrderStatuses.COMPLETED);
    }

    @Override
    public Collection getCancelledDriverOrders(Long driverId) {
        return getDriverOrdersByStatus(driverId, OrderStatuses.CANCELED);
    }

    @Override
    public Collection getOpenedUserOrders(Long userId) {
        return getUserOrdersByStatus(userId, OrderStatuses.OPENED);
    }

    @Override
    public Collection getAssignedUserOrders(Long userId) {
        return getUserOrdersByStatus(userId, OrderStatuses.ASSIGNED);
    }

    @Override
    public Collection getCancelledUserOrders(Long userId) {
        return getUserOrdersByStatus(userId, OrderStatuses.CANCELED);
    }

    @Override
    public Collection getCompletedUserOrders(Long userId) {
        return getUserOrdersByStatus(userId, OrderStatuses.COMPLETED);
    }

    @Override
    public Collection getOpenedOrders() {
        try {
            String sql = "select e from " + OrderHistory.class.getName() + " e " +
                    "where e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where e.order.id = r.order.id) " +
                    "and e.status.titleKey = :status";
            Query query = entityManager.createQuery(sql);
            query.setParameter("status", OrderStatuses.OPENED);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_ORDERS_FOUND_MESSAGE);
            return new ArrayList();
        }
    }

    @Override
    public Collection updateOrders(List<UpdateOrderDao> ordersToUpdate, TpUser updater) {
        List<Long> orderIds = ordersToUpdate.stream().
                map(UpdateOrderDao::getId).
                collect(Collectors.toList());
        Map<Long, UpdateOrderDao> updateOrderDaoMap = ordersToUpdate.stream().collect(Collectors.toMap(UpdateOrderDao::getId, Function.identity()));
        List<OrderHistory> orderHistories = getByIds(orderIds);
        Collection<OrderHistory> updatedOrderHistories = new ArrayList<>();
        for (OrderHistory orderHistory : orderHistories) {
            OrderHistory updateOrderHistory = new OrderHistory(orderHistory);
            UpdateOrderDao updateOrderDao = updateOrderDaoMap.get(orderHistory.getOrder().getId());
            TpUser driver = updateOrderDao.getDriverId() != null ?
                    userDetailsRepository.findUserAccount(updateOrderDao.getDriverId()) :
                    null;
            TpOrderStatus status = orderStatusRepository.getStatusById(updateOrderDao.getStatusId());
            if (driver == null) {
                if (!(status.getTitleKey().equals(OrderStatuses.ASSIGNED) || status.getTitleKey().equals(OrderStatuses.COMPLETED) || status.getTitleKey().equals(OrderStatuses.CANCELED))) {
                    updateOrderHistory.setDriver(driver);
                }
            } else {
                updateOrderHistory.setDriver(driver);
            }
            updateOrderHistory.setOrderStatus(orderStatusRepository.getStatusById(updateOrderDao.getStatusId()));
            updateOrderHistory.setUpdatedBy(updater);
            entityManager.persist(updateOrderHistory);
            updatedOrderHistories.add(updateOrderHistory);
        }
        entityManager.flush();
        return updatedOrderHistories;
    }

    private Collection getUserOrdersByStatus(Long userId, String status) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.order.client.userId = :userId and " +
                    "e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where r.order.client.userId = e.order.client.userId " +
                    "and e.order.id = r.order.id) " +
                    "and e.status.titleKey = :status";
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", userId);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_ORDERS_FOUND_MESSAGE);
            return new ArrayList();
        }
    }

    private Collection getDriverOrdersByStatus(Long driverId, String status) {
        try {
            String sql = "Select e from " + OrderHistory.class.getName() + " e " +
                    "where e.driver.userId = :driverId and " +
                    "e.updatedOn = (select max(r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where e.order.id = r.order.id) " +
                    "and e.status.titleKey = :status";
            Query query = entityManager.createQuery(sql);
            query.setParameter("driverId", driverId);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_ORDERS_FOUND_MESSAGE);
            return new ArrayList();
        }
    }

    private OrderHistory updateOrderStatus(OrderHistory orderDetails, String newStatus) {
        orderDetails.setOrderStatus(orderStatusRepository.getStatusByKey(newStatus));
        return orderHistoryRepository.createOrder(orderDetails);
    }

    private List<OrderHistory> getByIds(List<Long> orderIds) {
        try {
            String sql = "select oh from " + OrderHistory.class.getName() + " oh " +
                    "where oh.order.id in :orderIds " +
                    "and oh.updatedOn = (select max (r.updatedOn) from " + OrderHistory.class.getName() + " r " +
                    "where oh.order.id = r.order.id)";
            Query query = entityManager.createQuery(sql);
            query.setParameter("orderIds", orderIds);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.warn(NO_ORDERS_FOUND_MESSAGE);
            return new ArrayList<>();
        }
    }

}
