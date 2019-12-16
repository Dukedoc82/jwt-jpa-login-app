package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.HistoryRec;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.service.IOrderService;
import com.dyukov.taxi.service.IUserDetailsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api(value = "Controller for admin actions. Access permitted for the users with administrator role only.",
        description = "Controller for admin actions. Access permitted for the users with administrator role only.")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @ApiOperation(value = "Get a list of all orders in the system.", httpMethod = "GET", response = HistoryRec.class,
            responseContainer = "Collection")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = HistoryRec.class, responseContainer = "List")
    })
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public Collection getActualOrders() {
        return orderService.getActualOrders();
    }

    @ApiOperation(value = "Get a list of all users with any role in the system.", httpMethod = "GET",
            response = UserDao.class, responseContainer = "Collection")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = UserDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Collection getAllUsers() {
        return userDetailsService.findAll();
    }

    @ApiOperation(value = "Get a list of users who has a driver role in the system.", httpMethod = "GET",
            response = UserDao.class, responseContainer = "Collection")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = UserDao.class, responseContainer = "List")
    })
    @RequestMapping(value = "/driver", method = RequestMethod.GET)
    public Collection getDrivers() {
        return userDetailsService.findDrivers();
    }

    @ApiOperation(value = "Get a list of all users who has an administrator role in the system.", httpMethod = "GET",
            response = UserDao.class, responseContainer = "Collection")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = UserDao.class, responseContainer = "List")
    })
    @RequestMapping("/admin")
    public Collection getAdmins() {
        return userDetailsService.findAdmins();
    }

    @ApiOperation(value = "Get a list of all users who have driver role in the system.", httpMethod = "GET",
            response = UserDao.class, responseContainer = "Collection")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = UserDao.class, responseContainer = "List")
    })
    @RequestMapping("/simpleUser")
    public Collection getUsers() {
        return userDetailsService.findUsers();
    }

    @ApiOperation(value = "Get a list of all orders for the specified user.", httpMethod = "GET",
            response = HistoryRec.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = HistoryRec.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "User #%d doesn't exist.", response = UserNotFoundException.class),
    })
    @RequestMapping("/user/{userId}/order")
    public Collection getUserOrders(@ApiParam(hidden = true)
                                        @CookieValue(value = "userToken", defaultValue = "") String token,
                                    @ApiParam(value = "The id of the user to retrieve orders of.")
                                            @PathVariable("userId") Long userId) {
        Long retrieverUserId = tokenUtil.getUserIdFromToken(token);
        return orderService.getActualUserOrders(userId, retrieverUserId);
    }
}
