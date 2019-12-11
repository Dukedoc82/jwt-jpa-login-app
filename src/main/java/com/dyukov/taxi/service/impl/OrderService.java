package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.entity.ActualOrder;
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

    public ActualOrderDao createOrder(OrderDao orderDao, Long updatedBy) {
        return convertToDto(orderRepository.createOrder(convertFromDto(orderDao), updatedBy));
    }

    public ActualOrderDao getOrderById(Long id, Long retrieverUserId) {
        return convertToDto(orderRepository.getById(id, retrieverUserId));
    }

    public ActualOrderDao getOrderById(Long id) {
        return convertToDto(orderRepository.getById(id));
    }

    public Collection<ActualOrderDao> getActualOrders() {
        return convertToDto(orderRepository.getAll());
    }

    public ActualOrderDao assignOrderToDriver(ActualOrderDao orderDao, Long driverId, Long updatedBy) {
        ActualOrder actualOrder = orderRepository.getById(orderDao.getOrder().getId());
        if (actualOrder != null) {
            if (isOrderAssignable(actualOrder, driverId)) {
                return convertToDto(orderRepository.assignOrderToDriver(actualOrder.getOrder().getId(),
                        driverId, updatedBy));
            }
            return convertToDto(actualOrder);
        } else {
            throw new TaxiServiceException(1);
        }
    }

    private Collection<ActualOrderDao> convertToDto(Collection<ActualOrder> actualOrders) {
        return actualOrders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ActualOrderDao convertToDto(ActualOrder actualOrder) {
        return actualOrder == null ? null : modelMapper.map(actualOrder, ActualOrderDao.class);
    }

    private TpOrder convertFromDto(OrderDao orderDao) {
        TpOrder order = modelMapper.map(orderDao, TpOrder.class);
        order.setClient(userDetailsRepository.findUserAccount(orderDao.getClient().getUserId()));
        return order;
    }

    public ActualOrderDao cancelOrder(Long orderId, Long retrieverUserId) {
        ActualOrder actualOrder = orderRepository.getById(orderId);
        if (actualOrder.getOrder().getClient().getUserId().equals(retrieverUserId)) {
            if (isOrderCancellable(actualOrder)) {
                return convertToDto(orderRepository.cancelOrder(actualOrder));
            } else {
                return convertToDto(actualOrder);
            }
        } else {
            throw new TaxiServiceException(2);
        }
    }

    public ActualOrderDao completeOrder(Long orderId, Long driverId) {
        ActualOrder actualOrder = orderRepository.getById(orderId);
        if (actualOrder.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED)
                && driverId.equals(actualOrder.getDriver().getUserId())) {
            return convertToDto(orderRepository.completeOrder(actualOrder));
        } else {
            throw new TaxiServiceException(3);
        }
    }

    public ActualOrderDao refuseOrder(Long id, Long driverId) {
        ActualOrder actualOrder = orderRepository.getById(id);
        if (actualOrder.getStatus().getTitleKey().equals(OrderStatuses.ASSIGNED)
                && driverId.equals(actualOrder.getDriver().getUserId())) {
            return convertToDto(orderRepository.refuseOrder(actualOrder));
        } else {
            throw new TaxiServiceException(4);
        }
    }

    private boolean isOrderCancellable(ActualOrder actualOrder) {
        String status = actualOrder.getStatus().getTitleKey();
        return !status.equals(OrderStatuses.CANCELED) && !status.equals(OrderStatuses.COMPLETED);
    }

    private boolean isOrderAssignable(ActualOrder actualOrder, Long driverId) {
        String status = actualOrder.getStatus().getTitleKey();
        return !status.equals(OrderStatuses.CANCELED) && !status.equals(OrderStatuses.COMPLETED) &&
                !(status.equals(OrderStatuses.ASSIGNED) && actualOrder.getDriver().getUserId().equals(driverId));
    }

    public Collection<ActualOrderDao> getActualUserOrders(Long retrieverUserId) {
        return orderRepository.getAllUserOrders(retrieverUserId);
    }
}
