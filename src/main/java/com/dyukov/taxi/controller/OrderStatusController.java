package com.dyukov.taxi.controller;

import com.dyukov.taxi.dao.Status;
import com.dyukov.taxi.service.IOrderStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Api(value = "The controller which provides access to order status retrieval.",
        description = "The controller which provides access to order status retrieval.")
@RestController
@RequestMapping("/orderStatus")
public class OrderStatusController {

    @Autowired
    private IOrderStatusService orderStatusService;

    @ApiOperation(value = "Get all order statuses available in the system.", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Status.class, responseContainer = "List")
    })
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection<Status> getAvailableStatuses() {
        return orderStatusService.getAvailableStatuses();
    }

    @ApiOperation(value = "Get order status specified by key value.", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Status.class)
    })
    @RequestMapping(value = "/getByKey/{key}", method = RequestMethod.GET)
    public Status getStatusByKey(@PathVariable(name = "key") String key) {
        return orderStatusService.getStatusByKey(key);
    }
}
