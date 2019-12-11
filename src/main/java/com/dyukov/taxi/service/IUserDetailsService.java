package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.model.TpUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface IUserDetailsService {

    TpUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDao save(RegistrationData registrationData);

    UserDao saveAdmin(RegistrationData registrationData);

    UserDao saveDriver(RegistrationData registrationData);

    Collection<UserDao> findAll();


}
