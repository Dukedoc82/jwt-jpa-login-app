package com.dyukov.taxi.repository;

import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.TpUser;

import java.util.Collection;

public interface IUserDetailsRepository {

    TpUser findUserAccount(String userName);

    Collection findAll();

    TpUser findUserAccount(Long userId);

    TpUser saveUser(TpUser tpUser);

    TpUser saveAdmin(TpUser tpUser);

    TpUser saveDriver(TpUser tpUser);

    TpUser updateUser(TpUser tpUser);

    Collection findDrivers();

    Collection findAdmins();

    Collection findUsers();

    TpUser updatePassword(String userMail, String encodedPassword);
}
