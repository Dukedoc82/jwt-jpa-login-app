package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.HistoryRec;
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
    public HistoryRec assignOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                       @RequestBody HistoryRec historyRec) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);

        return orderService.assignOrderToDriver(historyRec.getOrder().getId(), retrieverUserId,
                retrieverUserId);
    }

    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    public HistoryRec completeOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                         @RequestBody HistoryRec historyRec) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.completeOrder(historyRec.getOrder().getId(), driverId);
    }

    @RequestMapping(value = "/refuseOrder", method = RequestMethod.POST)
    public HistoryRec refuseOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                       @RequestBody HistoryRec historyRec) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.refuseOrder(historyRec.getOrder().getId(), driverId);
    }

}
