package com.dyukov.taxi.service;

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

    public OrderDao createOrder(OrderDao orderDao, Long updatedBy) {
        TpOrder order = convertFromDto(orderDao);
        return convertToDto(orderRepository.createOrder(order, updatedBy));
    }

    public OrderDao getOrderById(Long id, Long retrieverUserId) {
        return convertToDto(orderRepository.getOrderById(id, retrieverUserId));
    }

    public OrderDao getOrderById(Long id) {
        return convertToDto(orderRepository.getOrderById(id));
    }

    public Collection<ActualOrderDao> getActualOrders() {
        return convertToDto(actualOrderRepository.getAll());
    }

    public ActualOrderDao assignOrderToDriver(OrderDao orderDao, Long driverId, Long updatedBy) {
        orderDao = convertToDto(orderRepository.getOrderById(orderDao.getId()));
        TpOrder order = convertFromDto(orderDao);
        return convertToDto(orderRepository.assignOrderToDriver(order, driverId, updatedBy));
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

    private OrderDao convertToDto(TpOrder order) {
        return order == null ? null : modelMapper.map(order, OrderDao.class);
    }

    private ActualOrder convertToDto(ActualOrderDao orderDao) {
        return orderDao == null ? null : modelMapper.map(orderDao, ActualOrder.class);
    }

    private ActualOrderDao convertFromDto(ActualOrder order) {
        return order == null ? null : modelMapper.map(order, ActualOrderDao.class);
    }
}
