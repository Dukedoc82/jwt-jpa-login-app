package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.entity.OrderHistory;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.exception.OrderNotFoundException;
import com.dyukov.taxi.exception.TaxiServiceException;
import com.dyukov.taxi.exception.WrongStatusOrder;
import com.dyukov.taxi.repository.IOrderRepository;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.service.IOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    public HistoryRec createOrder(OrderDao orderDao, Long updatedBy) {
        TpUser client = userDetailsRepository.findUserAccount(updatedBy);
        return convertToDto(orderRepository.createOrder(convertFromDto(orderDao), client));
    }

    public HistoryRec getOrderById(Long id, Long retrieverUserId) {
        TpUser retriever = userDetailsRepository.findUserAccount(retrieverUserId);
        OrderHistory order = orderRepository.getById(id, retrieverUserId);
        validateOrder(order, retriever);
        return convertToDto(orderRepository.getById(id, retrieverUserId));
    }

    public Collection getActualOrders() {
        return convertToDto(orderRepository.getAll());
    }

    public HistoryRec assignOrderToDriver(Long orderId, Long driverId, Long updatedBy) {
        OrderHistory orderHistory = orderRepository.getById(orderId);
        validateOrderAssignment(orderHistory, driverId);
        TpUser driver = userDetailsRepository.findUserAccount(driverId);
        TpUser updater = userDetailsRepository.findUserAccount(updatedBy);
        return convertToDto(orderRepository.assignOrderToDriver(orderHistory, driver, updater));
    }

    public HistoryRec cancelOrder(Long orderId, Long retrieverUserId) {
        TpUser retriever = userDetailsRepository.findUserAccount(retrieverUserId);
        OrderHistory orderHistory = orderRepository.getById(orderId);
        validateOrderCancellation(retriever, orderHistory);
        OrderHistory newRec = new OrderHistory(orderHistory.getOrder(), orderHistory.getDriver(), retriever);
        return convertToDto(orderRepository.cancelOrder(newRec));
    }

    public HistoryRec completeOrder(Long orderId, Long driverId) {
        OrderHistory orderDetails = orderRepository.getById(orderId, driverId);
        validateOrderCompletion(orderDetails, driverId);
        OrderHistory newRecord = new OrderHistory(orderDetails.getOrder(), orderDetails.getDriver(),
                userDetailsRepository.findUserAccount(driverId));
        return convertToDto(orderRepository.completeOrder(newRecord));
    }

    public HistoryRec refuseOrder(Long id, Long updaterId) {
        OrderHistory orderDetails = orderRepository.getById(id);
        TpUser updater = userDetailsRepository.findUserAccount(updaterId);
        validateOrderRefusal(orderDetails, updater);
        OrderHistory newRecord = new OrderHistory(orderDetails.getOrder(), null, updater);
        return convertToDto(orderRepository.refuseOrder(newRecord));
    }

    public Collection getActualUserOrders(Long userId) {
        TpUser user = userDetailsRepository.findUserAccount(userId);
        if (isAdmin(user)) {
            return orderRepository.getAll();
        }
        return orderRepository.getAllUserOrders(userId);
    }

    public Collection getActualUserOrders(Long userId, Long retrieverId) {
        if (retrieverId == null || isAdmin(userDetailsRepository.findUserAccount(retrieverId))) {
            return orderRepository.getAllUserOrders(userId);
        } else {
            return new ArrayList();
        }
    }

    @Override
    public Collection getDriverOrders(Long driverId) {
        return orderRepository.getDriverOrders(driverId);
    }

    @Override
    public Collection getAssignedDriverOrders(Long driverId) {
        return orderRepository.getAssignedDriverOrders(driverId);
    }

    @Override
    public Collection getCompletedDriverOrders(Long driverId) {
        return orderRepository.getCompletedDriverOrders(driverId);
    }

    @Override
    public Collection getCancelledDriverOrders(Long driverId) {
        return orderRepository.getCancelledDriverOrders(driverId);
    }

    @Override
    public Collection getOpenedUserOrders(Long userId) {
        return orderRepository.getOpenedUserOrders(userId);
    }

    private Collection<HistoryRec> convertToDto(Collection<OrderHistory> orderDetails) {
        return orderDetails.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private HistoryRec convertToDto(OrderHistory orderHistory) {
        return modelMapper.map(orderHistory, HistoryRec.class);
    }

    private TpOrder convertFromDto(OrderDao orderDao) {
        TpOrder order = modelMapper.map(orderDao, TpOrder.class);
        order.setClient(userDetailsRepository.findUserAccount(orderDao.getClient().getUserId()));
        return order;
    }

    private void validateOrderCancellation(TpUser user, OrderHistory orderHistory) {
        if (!isAdmin(user) && !orderHistory.getOrder().getClient().getUserId().equals(user.getUserId())) {
            throw new OrderNotFoundException(String.format(TaxiServiceException.ORDER_DOES_NOT_EXIST,
                    orderHistory.getOrder().getId()));
        }
        String status = orderHistory.getStatus().getTitleKey();
        if (status.equals(OrderStatuses.CANCELED) || status.equals(OrderStatuses.COMPLETED)) {
            throw new WrongStatusOrder(String.format(TaxiServiceException.WRONG_CANCELLATION_ORDER_STATUS,
                    orderHistory.getOrder().getId(), status));
        }
    }

    private void validateOrderAssignment(OrderHistory orderHistory, Long driverId) {
        String status = orderHistory.getStatus().getTitleKey();
        if (status.equals(OrderStatuses.CANCELED) || status.equals(OrderStatuses.COMPLETED))
            throw new WrongStatusOrder(String.format(TaxiServiceException.WRONG_ASSIGNMENT_ORDER_STATUS,
                    orderHistory.getOrder().getId(), status));
        TpUser driver = orderHistory.getDriver();
        if (status.equals(OrderStatuses.ASSIGNED) && driver != null && driver.getUserId().equals(driverId))
            throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_ALREADY_ASSIGNED,
                    orderHistory.getOrder().getId()));
    }

    private void validateOrderCompletion(OrderHistory orderDetails, Long driverId) {
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.COMPLETED))
            throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_ALREADY_COMPLETED,
                    orderDetails.getOrder().getId()));
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.OPENED)
                || orderDetails.getStatus().getTitleKey().equals(OrderStatuses.CANCELED))
            throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_NOT_ASSIGNED,
                    orderDetails.getOrder().getId()));
        if (orderDetails.getDriver() == null)
            throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_NOT_ASSIGNED_TO_DRIVER,
                    orderDetails.getOrder().getId()));
        if (!orderDetails.getDriver().getUserId().equals(driverId)) {
            TpUser user = userDetailsRepository.findUserAccount(driverId);
            if (!isAdmin(user)) {
                throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_NOT_ASSIGNED_TO_DRIVER,
                        orderDetails.getOrder().getId()));
            }
        }
    }

    private void validateOrderRefusal(OrderHistory orderHistory, TpUser updater) {
        if (!orderHistory.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED) || updater == null
                || (!isAdmin(updater) && !orderHistory.getDriver().getUserId().equals(updater.getUserId())))
            throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_NOT_ASSIGNED_TO_DRIVER,
                    orderHistory.getOrder().getId()));
    }

    private void validateOrder(OrderHistory order, TpUser retriever) {
        if (!order.getOrder().getClient().getUserId().equals(retriever.getUserId()) &&
                !isAdmin(retriever) &&
                !isAllowedDriver(order, retriever))
            throw new OrderNotFoundException(String.format(TaxiServiceException.ORDER_DOES_NOT_EXIST,
                    order.getOrder().getId()));
    }

    private boolean isAllowedDriver(OrderHistory order, TpUser retriever) {
        return isDriver(retriever) &&
                (order.getDriver() == null || order.getDriver().getUserId().equals(retriever.getUserId()));
    }

    private boolean isDriver(TpUser user) {
        return user.getRoleNames().contains("ROLE_DRIVER");
    }

    private boolean isAdmin(TpUser user) {
        return user.getRoleNames().contains("ROLE_ADMIN");
    }
}
