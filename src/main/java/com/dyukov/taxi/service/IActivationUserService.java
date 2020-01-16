package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.UserDao;

public interface IActivationUserService {

    void generateActivationToken(UserDao userDao);

    UserDao activateUser(String activationToken);

    int deleteExpiredActivationTokens();

}
