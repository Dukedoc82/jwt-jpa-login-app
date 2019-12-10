package com.dyukov.taxi.service;

import com.dyukov.taxi.config.OrderStatuses;
import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.entity.ActualOrder;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.repository.ActualOrderRepository;
import com.dyukov.taxi.repository.OrderRepository;
import com.dyukov.taxi.repository.UserDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ActualOrderRepository actualOrderRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ActualOrderDao createOrder(OrderDao orderDao, Long updatedBy) {
        return convertToDto(orderRepository.createOrder(convertFromDto(orderDao), updatedBy));
    }

    public ActualOrderDao getOrderById(Long id, Long retrieverUserId) {
        return convertToDto(orderRepository.getOrderById(id, retrieverUserId));
    }

    public ActualOrderDao getOrderById(Long id) {
        return convertToDto(orderRepository.getOrderById(id));
    }

    public Collection<ActualOrderDao> getActualOrders() {
        return convertToDto(actualOrderRepository.getAll());
    }

    public ActualOrderDao assignOrderToDriver(ActualOrderDao orderDao, Long driverId, Long updatedBy) {
        ActualOrder actualOrder = actualOrderRepository.getById(orderDao.getId());
        if (isOrderAssignable(actualOrder, driverId)) {
            return convertToDto(orderRepository.assignOrderToDriver(actualOrder.getOrder(), driverId, updatedBy));
        }
        return convertToDto(actualOrder);
    }

    private Collection<ActualOrderDao> convertToDto(List<ActualOrder> actualOrders) {
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

    private boolean isOrderAssignable(ActualOrder actualOrder, Long driverId) {
        String status = actualOrder.getStatus().getTitleKey();
        return !status.equals(OrderStatuses.CANCELED) && !status.equals(OrderStatuses.COMPLETED) &&
                !(status.equals(OrderStatuses.ASSIGNED) && actualOrder.getDriver().getUserId().equals(driverId));
    }
}
