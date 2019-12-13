package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.service.IOrderService;
import com.dyukov.taxi.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @RequestMapping("/order")
    public Collection getActualOrders() {
        return orderService.getActualOrders();
    }

    @RequestMapping("/user")
    public Collection getAllUsers() {
        return userDetailsService.findAll();
    }

    @RequestMapping("/driver")
    public Collection getDrivers() {
        return userDetailsService.findDrivers();
    }

    @RequestMapping("/admin")
    public Collection getAdmins() {
        return userDetailsService.findAdmins();
    }

    @RequestMapping("/simpleUser")
    public Collection getUsers() {
        return userDetailsService.findUsers();
    }

    @RequestMapping("/user/{userId}/order")
    public Collection getUserOrders(@CookieValue(value = "userToken", defaultValue = "") String token,
                                    @PathVariable("userId") Long userId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getActualUserOrders(userId, retrieverUserId);
    }
}
