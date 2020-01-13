package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.model.LoginRequest;
import com.dyukov.taxi.model.TpUserDetails;
import com.dyukov.taxi.service.ITokenService;
import com.dyukov.taxi.service.IUserDetailsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Api(value = "Controller for authentication and registration endpoints.",
        description = "Controller for authentication and registration endpoints.")
@RestController
@CrossOrigin
public class JwtAuthenticationController {

    public class AuthData {

        private String uri;

        private UserDao user;

        AuthData(String uri, UserDao user) {
            this.uri = uri;
            this.user = user;
        }

        public String getUri() {
            return uri;
        }

        public UserDao getUser() {
            return user;
        }
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IUserDetailsService userDetailsService;

    @Autowired
    private ITokenService tokenService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final TpUserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        String targetUrl = "/";
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            switch (authority.getAuthority()) {
                case "ROLE_ADMIN":
                    targetUrl = getAdminUrl(targetUrl);
                    break;
                case "ROLE_DRIVER":
                    targetUrl = getDriverUrl(targetUrl);
                    break;
                case "ROLE_USER":
                    targetUrl = getUserUrl(targetUrl);
                    break;
            }
        }
        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "usertoken")
                .header("usertoken", token)
                .body(new AuthData(targetUrl, userDetails.getUser()));
    }

    private String getAdminUrl(String targetUrl) {
        targetUrl = targetUrl + "admin/test.html";
        return targetUrl;
    }

    private String getDriverUrl(String targetUrl) {
        targetUrl = targetUrl + "driver/test.html";
        return targetUrl;
    }

    private String getUserUrl(String targetUrl) {
        targetUrl = targetUrl + "test.html";
        return targetUrl;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public UserDao registerUser(@RequestBody RegistrationData registrationData) {
        return userDetailsService.save(registrationData);
    }

    @RequestMapping(value = "/registerAsADriver", method = RequestMethod.POST)
    public UserDao registerAdmin(@RequestBody RegistrationData registrationData) {
        return userDetailsService.saveDriver(registrationData);
    }

    @RequestMapping(value = "/doLogout", method = RequestMethod.GET)
    public void doLogout(@RequestHeader("userToken") String token) {
        tokenService.addTokenToBlackList(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
