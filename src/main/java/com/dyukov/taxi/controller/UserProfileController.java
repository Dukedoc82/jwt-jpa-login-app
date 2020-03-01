package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private IUserDetailsService userDetailsService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public UserDao updateProfile(@RequestHeader("usertoken") String token,
                                 @RequestBody RegistrationData profile) {
        return userDetailsService.updateProfile(tokenUtil.getUserIdFromToken(token), profile);

    }
}
