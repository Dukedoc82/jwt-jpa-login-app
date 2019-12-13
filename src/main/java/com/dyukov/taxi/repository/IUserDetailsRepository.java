package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpUser;

import java.util.Collection;

public interface IUserDetailsRepository {

    TpUser findUserAccount(String userName);

    Collection findAll();

    TpUser findUserAccount(Long userId);

    TpUser saveUser(TpUser tpUser);

    TpUser saveAdmin(TpUser tpUser);

    TpUser saveDriver(TpUser tpUser);
}
