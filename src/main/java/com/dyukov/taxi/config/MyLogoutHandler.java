package com.dyukov.taxi.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class MyLogoutHandler implements LogoutHandler {    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            System.out.println("Key: " + key);
            System.out.println("Value: " + request.getHeader(key));
        }
    }
}
