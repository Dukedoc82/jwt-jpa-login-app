package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.entity.OrderDetails;
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
        return convertToDto(orderRepository.getById(id, retrieverUserId, isDriver(retriever)));
    }

    public HistoryRec getOrderById(Long id) {
        return convertToDto(orderRepository.getById(id));
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
        return convertToDto(orderRepository.cancelOrder(orderHistory));
    }

    public HistoryRec completeOrder(Long orderId, Long driverId) {
        TpUser driver = userDetailsRepository.findUserAccount(driverId);
        OrderHistory orderDetails = orderRepository.getById(orderId, driverId, isDriver(driver));
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED)
                && driverId.equals(orderDetails.getDriver().getUserId())) {
            return convertToDto(orderRepository.completeOrder(orderDetails));
        } else {
            throw new TaxiServiceException(3);
        }
    }

    public HistoryRec completeOrderAsAdmin(Long orderId) {
        OrderHistory history = orderRepository.getById(orderId);
        return convertToDto(history);
    }

    public HistoryRec refuseOrder(Long id, Long driverId) {
        OrderHistory orderDetails = orderRepository.getById(id);
        TpUser driver = userDetailsRepository.findUserAccount(driverId);
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED) && driver != null
                && driverId.equals(driver.getUserId())) {
            return convertToDto(orderRepository.refuseOrder(orderDetails, driver));
        } else {
            throw new TaxiServiceException(4);
        }
    }

    public Collection getActualUserOrders(Long retrieverUserId) {
        return orderRepository.getAllUserOrders(retrieverUserId);
    }

    private Collection<HistoryRec> convertToDto(Collection<OrderHistory> orderDetails) {
        return orderDetails.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private HistoryRec convertToDto(OrderHistory orderHistory) {
        return modelMapper.map(orderHistory, HistoryRec.class);
    }

    private OrderDetailsDao convertToDto(OrderDetails orderDetails) {
        return orderDetails == null ? null : modelMapper.map(orderDetails, OrderDetailsDao.class);
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

    private void validateOrderCompletion(OrderDetails orderDetails) {
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.COMPLETED))
            throw new WrongStatusOrder(String.format(TaxiServiceException.ORDER_IS_ALREADY_COMPLETED,
                    orderDetails.getOrder().getId()));


    }

    private boolean isDriver(TpUser user) {
        return user.getRoleNames().contains("ROLE_DRIVER");
    }

    private boolean isAdmin(TpUser user) {
        return user.getRoleNames().contains("ROLE_ADMIN");
    }
}
