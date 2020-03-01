package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.dao.UserEditableDataDao;
import com.dyukov.taxi.dao.UserRolesDao;
import com.dyukov.taxi.model.TpUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface IUserDetailsService {

    TpUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    TpUserDetails findUser(Long id);

    UserDao save(RegistrationData registrationData);

    UserDao saveAdmin(RegistrationData registrationData);

    UserDao saveDriver(RegistrationData registrationData);

    Collection findAll();

    Collection findDrivers();

    Collection findAdmins();

    Collection findUsers();

    UserRolesDao getUserRoles(Long userIdFromToken);

    UserEditableDataDao getEditableUserData(Long userId);

    UserEditableDataDao updateUser(UserEditableDataDao userEditableDataDao);

    UserDao getMe(Long userIdFromToken);

    UserDao updateProfile(Long userIdFromToken, RegistrationData profile);
}
