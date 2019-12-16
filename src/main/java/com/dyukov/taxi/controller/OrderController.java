package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.exception.OrderNotFoundException;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.exception.WrongStatusOrder;
import com.dyukov.taxi.service.IOrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;

@RestController
@RequestMapping("/order")
@Api(value = "The controller which handles different types of actions with orders",
        consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public HistoryRec createOrder(@ApiParam(value = "Token value of the logged in user. Cookie param.", required = true)
                                        @CookieValue(value = "userToken", defaultValue = "") String token,
                                  @ApiParam(value = "Order details to persist.", required = true)
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
    public HistoryRec getOrderById(@ApiParam(hidden = true) @CookieValue(value = "userToken", defaultValue = "") String token,
                                   @ApiParam(value = "The id of the order to retrieve information about.")
                                       @PathVariable("id") Long orderId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getOrderById(orderId, retrieverUserId);
    }

    @ApiOperation(httpMethod = "POST", value = "Cancel the order specified by the order record.",
            response = HistoryRec.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = HistoryRec.class),
            @ApiResponse(code = 500, message = "Cannot cancel cancelled or assigned order. Order #%d is %s.",
                    response = WrongStatusOrder.class)
    })
    @RequestMapping(value = "/cancel/{orderId}", method = RequestMethod.POST)
    public HistoryRec cancelOrder(@ApiParam(hidden = true)
                                      @CookieValue(value = "userToken", defaultValue = "") String token,
                                  @ApiParam(value = "Order id to cancel.", required = true, example = "5")
                                      @PathVariable(value = "orderId") Long orderId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.cancelOrder(orderId, retrieverUserId);
    }

    @ApiOperation(value = "Get all user orders.", response = Collection.class, httpMethod = "GET")
    @ApiResponse(code = 200, message = "Success", response = Collection.class)
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Collection getUserOrders(@ApiParam(hidden = true)
                                        @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getActualUserOrders(retrieverUserId);
    }

    @ApiOperation(value = "Get orders in the opened status of the currently logged in user.",
            response = Collection.class, httpMethod = "GET")
    @ApiResponse(code = 200, message = "Success", response = Collection.class)
    @RequestMapping(value = "/opened", method = RequestMethod.GET)
    public Collection getUserOpenedOrders(@ApiParam(hidden = true)
                                              @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getOpenedUserOrders(retrieverUserId);
    }

    @ApiOperation(value = "Get orders in the assigned status of the currently logged in user.",
            response = Collection.class, httpMethod = "GET")
    @ApiResponse(code = 200, message = "Success", response = Collection.class)
    @RequestMapping(value = "/assigned", method = RequestMethod.GET)
    public Collection getUserAssignedOrders(@ApiParam(hidden = true)
                                                @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getAssignedUserOrders(retrieverUserId);
    }

    @ApiOperation(value = "Get orders in the cancelled status of the currently logged in user.",
            response = Collection.class, httpMethod = "GET")
    @ApiResponse(code = 200, message = "Success", response = Collection.class)
    @RequestMapping(value = "/cancelled", method = RequestMethod.GET)
    public Collection getUserCancelledOrders(@ApiParam(hidden = true)
                                                 @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getCancelledUserOrders(retrieverUserId);
    }

    @ApiOperation(value = "Get orders in the completed status of the currently logged in user.",
            response = Collection.class, httpMethod = "GET")
    @ApiResponse(code = 200, message = "Success", response = Collection.class)
    @RequestMapping(value = "/completed")
    public Collection getUserCompletedOrders(@ApiParam(hidden = true)
                                                 @CookieValue(value = "userToken", defaultValue = "") String token) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getCompletedUserOrders(retrieverUserId);
    }

    private void addUserData(String token, OrderDao order) {
        UserDao client = new UserDao();
        client.setUserId(tokenUtil.getUserIdFromToken(token));
        order.setClient(client);
    }

}
