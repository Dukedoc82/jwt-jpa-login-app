package com.dyukov.taxi.controller;

import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.service.JwtUserDetailsService;
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

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping("/actualOrder")
    public Collection<ActualOrderDao> getActualOrders() {
        return orderService.getActualOrders();
    }

    @RequestMapping(value = "/order/{id}")
    public ActualOrderDao getOrderById(@PathVariable("id") Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @RequestMapping("/user")
    public Collection<UserDao> getAllUsers() {
        return userDetailsService.findAll();
    }
}
