package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import com.dyukov.taxi.service.IActivationUserService;
import com.dyukov.taxi.service.IMailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.UUID;

@Service
public class ActivationUserService implements IActivationUserService {

    @Autowired
    IActivationTokenRepository activationTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IMailService mailService;

    @Autowired
    private Environment environment;

    @Override
    public void generateActivationToken(UserDao userDao) {
        String token = UUID.randomUUID().toString();
        activationTokenRepository.persistActivationToken(token, convertFromDTO(userDao));
        //String activationUrl = environment.
        //mailService.sendActivationEmail(token);
    }

    private UserDao convertToDTO(TpUser tpUser) {
        return modelMapper.map(tpUser, UserDao.class);
    }

    private TpUser convertFromDTO(UserDao userDao) {
        return modelMapper.map(userDao, TpUser.class);
    }
}
