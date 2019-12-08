package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.entity.TpOrder;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.repository.OrderRepository;
import com.dyukov.taxi.repository.UserDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public OrderDao createOrder(OrderDao orderDao, Long updatedBy) {
        TpOrder order = convertFromDto(orderDao);
        return convertToDto(orderRepository.createOrder(order, updatedBy));
    }

    public OrderDao getOrderById(Long id, Long retrieverUserId) {
        return convertToDto(orderRepository.getOrderById(id, retrieverUserId));
    }

    private TpOrder convertFromDto(OrderDao orderDao) {
        TpOrder order = modelMapper.map(orderDao, TpOrder.class);
        order.setClient(userDetailsRepository.findUserAccount(orderDao.getClient().getUserId()));
        return order;
    }

    private OrderDao convertToDto(TpOrder order) {
        return order == null ? null : modelMapper.map(order, OrderDao.class);
    }
}
