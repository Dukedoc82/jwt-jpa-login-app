package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "new", method = RequestMethod.POST)
    public ActualOrderDao createOrder(@CookieValue(value = "userToken", defaultValue = "") String token, @RequestBody OrderDao order) {
        addUserData(token, order);
        return orderService.createOrder(order, tokenUtil.getUserIdFromToken(token));
    }

    @RequestMapping(value = "/{id}")
    public ActualOrderDao getOrderById(@CookieValue(value = "userToken", defaultValue = "") String token, @PathVariable("id") Long orderId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getOrderById(orderId, retrieverUserId);
    }

    private void addUserData(String token, OrderDao order) {
        UserDao client = new UserDao();
        client.setUserId(tokenUtil.getUserIdFromToken(token));
        order.setClient(client);
    }

}
