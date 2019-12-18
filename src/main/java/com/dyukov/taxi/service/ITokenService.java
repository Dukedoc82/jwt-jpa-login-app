package com.dyukov.taxi.service;

import com.dyukov.taxi.entity.ExpiredToken;

public interface ITokenService {

    ExpiredToken addTokenToBlackList(String token);

    ExpiredToken getTokenFromBlacklist(String token);
}
