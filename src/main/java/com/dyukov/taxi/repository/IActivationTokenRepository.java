package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;

public interface IActivationTokenRepository {

    ActivationToken persistActivationToken(String value, TpUser user);

    ActivationToken findToken(String tokenValue);

    int deleteToken(String tokenValue);

    int deleteOutdatedTokens();
}
