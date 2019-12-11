package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "new", method = RequestMethod.POST)
    public ActualOrderDao createOrder(@CookieValue(value = "userToken", defaultValue = "") String token, @RequestBody OrderDao order) {
        addUserData(token, order);
        return orderService.createOrder(order, tokenUtil.getUserIdFromToken(token));
    }

    @RequestMapping(value = "/{id}")
    public ActualOrderDao getOrderById(@CookieValue(value = "userToken", defaultValue = "") String token,
                                       @PathVariable("id") Long orderId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getOrderById(orderId, retrieverUserId);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ActualOrderDao cancelOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                      @RequestBody ActualOrderDao orderDao) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.cancelOrder(orderDao.getOrder().getId(), retrieverUserId);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public Collection<ActualOrderDao> getUserOrders(@CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getActualUserOrders(retrieverUserId);
    }

    private void addUserData(String token, OrderDao order) {
        UserDao client = new UserDao();
        client.setUserId(tokenUtil.getUserIdFromToken(token));
        order.setClient(client);
    }

}
