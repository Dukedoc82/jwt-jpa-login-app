package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverActionsController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @RequestMapping(value = "/assignOrderToMe", method = RequestMethod.POST)
    public OrderDetailsDao assignOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                       @RequestBody OrderDetailsDao orderDao) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);

        return orderService.assignOrderToDriver(orderDao.getOrder().getId(), retrieverUserId, retrieverUserId);
    }

    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    public OrderDetailsDao completeOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                         @RequestBody OrderDetailsDao orderDao) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.completeOrder(orderDao.getOrder().getId(), driverId);
    }

    @RequestMapping(value = "/refuseOrder", method = RequestMethod.POST)
    public OrderDetailsDao refuseOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                       @RequestBody OrderDetailsDao orderDao) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.refuseOrder(orderDao.getOrder().getId(), driverId);
    }

}
