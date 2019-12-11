package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.ActualOrderDao;
import com.dyukov.taxi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverActionsController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @RequestMapping(value = "/assignOrderToMe", method = RequestMethod.POST)
    public ActualOrderDao assignOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                      @RequestBody ActualOrderDao orderDao) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);

        return orderService.assignOrderToDriver(orderDao, retrieverUserId, retrieverUserId);
    }

    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    public ActualOrderDao completeOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                        @RequestBody ActualOrderDao orderDao) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.completeOrder(orderDao.getId(), driverId);
    }

    @RequestMapping(value = "/refuseOrder", method = RequestMethod.POST)
    public ActualOrderDao refuseOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                      @RequestBody ActualOrderDao orderDao) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.refuseOrder(orderDao.getId(), driverId);
    }

}
