package com.dyukov.taxi.controller;

import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping({"/hello"})
    public String firstPage() {
        return "Hello World";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public UserDao registerUser(@RequestBody UserDao userDao) {
        System.out.println("!!!!! userDao: " + userDao);
        return userDetailsService.save(userDao);
    }
}
