package com.dyukov.taxi.controller;

import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/orderStatus")
public class OrderStatusController {

    @Autowired
    private OrderStatusService orderStatusService;

    @RequestMapping("/")
    public Collection<TpOrderStatus> getAvailableStatuses() {
        return orderStatusService.getAvailableStatuses();
    }

    @RequestMapping("/getByKey/{key}")
    public TpOrderStatus getStatusByKey(@PathVariable(name = "key") String key) {
        return orderStatusService.getStatusByKey(key);
    }
}
