package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.entity.OrderDetails;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.exception.TaxiServiceException;
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

    public OrderDetailsDao createOrder(OrderDao orderDao, Long updatedBy) {
        return convertToDto(orderRepository.createOrder(convertFromDto(orderDao), updatedBy));
    }

    public OrderDetailsDao getOrderById(Long id, Long retrieverUserId) {
        return convertToDto(orderRepository.getById(id, retrieverUserId));
    }

    public OrderDetailsDao getOrderById(Long id) {
        return convertToDto(orderRepository.getById(id));
    }

    public Collection<OrderDetailsDao> getActualOrders() {
        return convertToDto(orderRepository.getAll());
    }

    public OrderDetailsDao assignOrderToDriver(OrderDetailsDao orderDao, Long driverId, Long updatedBy) {
        OrderDetails orderDetails = orderRepository.getById(orderDao.getOrder().getId());
        if (orderDetails != null) {
            if (isOrderAssignable(orderDetails, driverId)) {
                return convertToDto(orderRepository.assignOrderToDriver(orderDetails.getOrder().getId(),
                        driverId, updatedBy));
            }
            return convertToDto(orderDetails);
        } else {
            throw new TaxiServiceException(1);
        }
    }

    private Collection<OrderDetailsDao> convertToDto(Collection<OrderDetails> orderDetails) {
        return orderDetails.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private OrderDetailsDao convertToDto(OrderDetails orderDetails) {
        return orderDetails == null ? null : modelMapper.map(orderDetails, OrderDetailsDao.class);
    }

    private TpOrder convertFromDto(OrderDao orderDao) {
        TpOrder order = modelMapper.map(orderDao, TpOrder.class);
        order.setClient(userDetailsRepository.findUserAccount(orderDao.getClient().getUserId()));
        return order;
    }

    public OrderDetailsDao cancelOrder(Long orderId, Long retrieverUserId) {
        OrderDetails orderDetails = orderRepository.getById(orderId);
        if (orderDetails.getOrder().getClient().getUserId().equals(retrieverUserId)) {
            if (isOrderCancellable(orderDetails)) {
                return convertToDto(orderRepository.cancelOrder(orderDetails));
            } else {
                return convertToDto(orderDetails);
            }
        } else {
            throw new TaxiServiceException(2);
        }
    }

    public OrderDetailsDao completeOrder(Long orderId, Long driverId) {
        OrderDetails orderDetails = orderRepository.getById(orderId);
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED)
                && driverId.equals(orderDetails.getDriver().getUserId())) {
            return convertToDto(orderRepository.completeOrder(orderDetails));
        } else {
            throw new TaxiServiceException(3);
        }
    }

    public OrderDetailsDao refuseOrder(Long id, Long driverId) {
        OrderDetails orderDetails = orderRepository.getById(id);
        if (orderDetails.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED)
                && driverId.equals(orderDetails.getDriver().getUserId())) {
            return convertToDto(orderRepository.refuseOrder(orderDetails));
        } else {
            throw new TaxiServiceException(4);
        }
    }

    private boolean isOrderCancellable(OrderDetails orderDetails) {
        String status = orderDetails.getStatus().getTitleKey();
        return !status.equals(OrderStatuses.CANCELED) && !status.equals(OrderStatuses.COMPLETED);
    }

    private boolean isOrderAssignable(OrderDetails orderDetails, Long driverId) {
        String status = orderDetails.getStatus().getTitleKey();
        return !status.equals(OrderStatuses.CANCELED) && !status.equals(OrderStatuses.COMPLETED) &&
                !(status.equals(OrderStatuses.ASSIGNED) && orderDetails.getDriver().getUserId().equals(driverId));
    }

    public Collection<OrderDetailsDao> getActualUserOrders(Long retrieverUserId) {
        return orderRepository.getAllUserOrders(retrieverUserId);
    }
}
