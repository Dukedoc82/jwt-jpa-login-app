package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.entity.ExpiredToken;
import com.dyukov.taxi.repository.impl.ExpiredTokensRepository;
import com.dyukov.taxi.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements ITokenService {

    @Autowired
    private ExpiredTokensRepository tokensRepository;

    @Override
    public ExpiredToken addTokenToBlackList(String token) {
        return tokensRepository.addToBlackList(token);
    }

    @Override
    public ExpiredToken getTokenFromBlacklist(String token) {
        return tokensRepository.getFromBlackList(token);
    }
}
