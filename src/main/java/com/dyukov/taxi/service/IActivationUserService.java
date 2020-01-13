package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.UserDao;

public interface IActivationUserService {

    public void generateActivationToken(UserDao userDao);
}
