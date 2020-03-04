package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.ActivationToken;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.exception.InvalidRegistrationTokenException;
import com.dyukov.taxi.repository.IActivationTokenRepository;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.service.IActivationUserService;
import com.dyukov.taxi.service.IMailService;
import com.dyukov.taxi.utils.EncryptedPasswordUtils;
import com.dyukov.taxi.utils.IPasswordUtils;
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

    @Autowired
    private IPasswordUtils passwordUtils;

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

    @Override
    public String generateAndSendNewPassword(String token) {
        ActivationToken requestToken = activationTokenRepository.findToken(token);
        TpUser requestUser = requestToken.getUser();
        if (requestUser != null) {
            String newPassword = passwordUtils.generateRandomPassword();
            String encodedPassword = EncryptedPasswordUtils.encryptePassword(newPassword);
            UserDao user = convertToDTO(userDetailsRepository.updatePassword(requestUser.getUserName(), encodedPassword));
            mailService.sendNewPassword(requestUser.getUserName(), newPassword);
            activationTokenRepository.deleteToken(token);
            return user.getUserName();
        } else {
            throw new InvalidRegistrationTokenException("No token " + token + " found.");
        }
    }

    @Override
    public void requestNewPassword(String userMail) throws Exception {
        String token = UUID.randomUUID().toString();
        TpUser user = userDetailsRepository.findUserAccount(userMail);
        if (!user.isEnabled()) {
            throw new Exception("User " + userMail + " is not activated.");
        }
        UserDao userDao = convertToDTO(userDetailsRepository.findUserAccount(userMail));
        activationTokenRepository.persistActivationToken(token, convertFromDTO(userDao));
        mailService.sendNewPasswordRequestTokenMail(userDao.getUserName(), token);

    }

    private TpUser convertFromDTO(UserDao userDao) {
        return modelMapper.map(userDao, TpUser.class);
    }

    private UserDao convertToDTO(TpUser tpUser) {
        return modelMapper.map(tpUser, UserDao.class);
    }
}
