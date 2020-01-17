package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.service.IActivationUserService;
import com.dyukov.taxi.service.IMailService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivationUserService implements IActivationUserService {

    private static final Logger logger = LoggerFactory.getLogger(ActivationUserService.class);

    @Autowired
    private IActivationTokenRepository activationTokenRepository;

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IMailService mailService;

    @Override
    public void generateActivationToken(UserDao userDao) {
        String token = UUID.randomUUID().toString();
        activationTokenRepository.persistActivationToken(token, convertFromDTO(userDao));
        mailService.sendRegistrationConfirmationEmail(userDao.getUserName(), token);
    }

    @Override
    public UserDao activateUser(String activationToken) {
        ActivationToken token = activationTokenRepository.findToken(activationToken);
        TpUser activationUser = token.getUser();
        activationUser.setEnabled(true);
        TpUser activatedUser = userDetailsRepository.updateUser(activationUser);
        activationTokenRepository.deleteToken(activationToken);
        return convertToDTO(activatedUser);
    }

    @Override
    public int deleteExpiredActivationTokens() {
        return activationTokenRepository.deleteOutdatedTokens();
    }

    private TpUser convertFromDTO(UserDao userDao) {
        return modelMapper.map(userDao, TpUser.class);
    }

    private UserDao convertToDTO(TpUser tpUser) {
        return modelMapper.map(tpUser, UserDao.class);
    }
}
