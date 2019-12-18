package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.ExpiredToken;

public interface IExpiredTokensRepository {

    ExpiredToken addToBlackList(String token);

    ExpiredToken getFromBlackList(String token);

}
