package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.exception.OrderNotFoundException;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;

@RestController
@RequestMapping("/order")
@Api(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private IOrderService orderService;

    @ApiOperation(httpMethod = "POST", value = "Create a new order.", response = HistoryRec.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = HistoryRec.class),
            @ApiResponse(code = 500, message = "User #%d doesn't exist.", response = UserNotFoundException.class)
    })
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public HistoryRec createOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                  @RequestBody OrderDao order) {
        addUserData(token, order);
        return orderService.createOrder(order, tokenUtil.getUserIdFromToken(token));
    }

    @ApiOperation(httpMethod = "GET", value = "Get order details by order id.", response = HistoryRec.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = HistoryRec.class),
            @ApiResponse(code = 500, message = "User #%d doesn't exist.", response = UserNotFoundException.class),
            @ApiResponse(code = 500, message = "Order #%d doesn't exist.", response = OrderNotFoundException.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = HttpClientErrorException.Unauthorized.class)
    })
    @RequestMapping(value = "/{id}")
    public HistoryRec getOrderById(@CookieValue(value = "userToken", defaultValue = "") String token,
                                        @PathVariable("id") Long orderId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getOrderById(orderId, retrieverUserId);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public HistoryRec cancelOrder(@CookieValue(value = "userToken", defaultValue = "") String token,
                                       @RequestBody HistoryRec historyRec) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.cancelOrder(historyRec.getOrder().getId(), retrieverUserId);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection getUserOrders(@CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getActualUserOrders(retrieverUserId);
    }

    @RequestMapping(value = "/opened")
    public Collection getUserOpenedOrders(@CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getOpenedUserOrders(retrieverUserId);
    }

    private void addUserData(String token, OrderDao order) {
        UserDao client = new UserDao();
        client.setUserId(tokenUtil.getUserIdFromToken(token));
        order.setClient(client);
    }

}
