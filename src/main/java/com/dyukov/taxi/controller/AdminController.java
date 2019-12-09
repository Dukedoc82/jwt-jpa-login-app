package com.dyukov.taxi.controller;

import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/actualOrder")
    public Collection<ActualOrderDao> getActualOrders() {
        return orderService.getActualOrders();
    }

    @RequestMapping(value = "/order/{id}")
    public OrderDao getOrderById(@PathVariable("id") Long orderId) {
        return orderService.getOrderById(orderId);
    }
}
