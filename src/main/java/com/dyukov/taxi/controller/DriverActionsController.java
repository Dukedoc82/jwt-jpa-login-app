package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.exception.OrderNotFoundException;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.exception.WrongStatusOrder;
import com.dyukov.taxi.service.IOrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api(value = "Controller for driver actions. Access permitted for the users with driver and administrator roles only.",
        description = "Controller for driver actions. Access permitted for the users with driver and administrator roles only.")
@RestController
@RequestMapping("/driver")
public class DriverActionsController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @ApiOperation(value = "Assign the order specified by the orderId to the currently logged in user",
            httpMethod = "PUT", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class),
            @ApiResponse(code = 500, message = "User #%d doesn't exist.", response = UserNotFoundException.class),
            @ApiResponse(code = 500, message = "Order #%d doesn't exist.", response = OrderNotFoundException.class),
    })
    @RequestMapping(value = "/assignOrderToMe/{id}", method = RequestMethod.PUT)
    public OrderDetailsDao assignOrder(@ApiParam(hidden = true)
                                      @RequestHeader("usertoken") String token,
                                       @ApiParam(name = "id", value = "Order id to assign to the currently logged in user.")
                                  @PathVariable("id") Long orderId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.assignOrderToDriver(orderId, retrieverUserId, retrieverUserId);
    }

    @ApiOperation(value = "Complete the order specified by the orderId to the currently logged in user",
            httpMethod = "PUT", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class),
            @ApiResponse(code = 500, message = "User #%d doesn't exist.", response = UserNotFoundException.class),
            @ApiResponse(code = 500, message = "Order #%d doesn't exist.", response = OrderNotFoundException.class),
            @ApiResponse(code = 500, message = "Cannot assign cancelled or completed order. Order #%d is %s.",
                    response = WrongStatusOrder.class),
            @ApiResponse(code = 500, message = "Order #%d is not assigned to you.", response = WrongStatusOrder.class)
    })
    @RequestMapping(value = "/completeOrder/{id}", method = RequestMethod.PUT)
    public OrderDetailsDao completeOrder(@ApiParam(hidden = true)
                                        @CookieValue(value = "userToken", defaultValue = "") String token,
                                         @ApiParam(name = "id", value = "Order id to mark as the completed.")
                                        @PathVariable("id") Long orderId) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.completeOrder(orderId, driverId);
    }

    @ApiOperation(value = "Refuse the order specified by the orderId to the currently logged in user",
            httpMethod = "PUT", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class),
            @ApiResponse(code = 500, message = "User #%d doesn't exist.", response = UserNotFoundException.class),
            @ApiResponse(code = 500, message = "Order #%d doesn't exist.", response = OrderNotFoundException.class),
            @ApiResponse(code = 500, message = "Order #%d is not assigned to you.", response = WrongStatusOrder.class)
    })
    @RequestMapping(value = "/refuseOrder", method = RequestMethod.PUT)
    public OrderDetailsDao refuseOrder(@ApiParam(hidden = true)
                                      @CookieValue(value = "userToken", defaultValue = "") String token,
                                       @ApiParam(name = "id", value = "Order id to refuse.")
                                      @PathVariable("id") Long orderId) {
        Long updaterId = tokenUtil.getUserIdFromToken(token);
        return orderService.refuseOrder(orderId, updaterId);
    }

    @ApiOperation(value = "List the orders assigned to the currently logged in user in any status.",
            httpMethod = "GET", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/myOrders", method = RequestMethod.GET)
    public Collection getDriverOrders(@ApiParam(hidden = true)
                                          @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.getDriverOrders(driverId);
    }

    @ApiOperation(value = "List all the orders assigned to the currently logged in user in the assigned status.",
            httpMethod = "GET", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/assignedOrders", method = RequestMethod.GET)
    public Collection getAssignedOrders(@ApiParam(hidden = true)
                                            @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.getAssignedDriverOrders(driverId);
    }

    @ApiOperation(value = "List all the orders assigned to the currently logged in user in the completed status.",
            httpMethod = "GET", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/completedOrders", method = RequestMethod.GET)
    public Collection getCompletedOrders(@ApiParam(hidden = true)
                                             @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.getCompletedDriverOrders(driverId);
    }

    @ApiOperation(value = "List all the orders assigned to the currently logged in user in the cancelled status.",
            httpMethod = "GET", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/cancelledOrders", method = RequestMethod.GET)
    public Collection getCancelledOrders(@ApiParam(hidden = true)
                                             @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long driverId = tokenUtil.getUserIdFromToken(token);
        return orderService.getCancelledDriverOrders(driverId);
    }

    @ApiOperation(value = "List all the opened orders which are available to be assigned.",
            httpMethod = "GET", response = OrderDetailsDao.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = OrderDetailsDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/openedOrders")
    public Collection getOpenedOrders() {
        return orderService.getOpenedOrders();
    }

}
