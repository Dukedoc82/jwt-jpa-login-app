package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.MailSettingsDao;
import com.dyukov.taxi.dao.UserDao;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserMailSettings;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.repository.IUserMailSettingsRepository;
import com.dyukov.taxi.service.IMailSettingsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailSettingsService implements IMailSettingsService {

    @Autowired
    private IUserMailSettingsRepository mailSettingsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Override
    public MailSettingsDao updateSettings(Long userId, MailSettingsDao mailSettingsDao) {
        TpUser user = userDetailsRepository.findUserAccount(userId);
        UserMailSettings currentSettings = mailSettingsRepository.getSettingsByUser(user);
        if (mailSettingsDao.getGetNewOrderNotifications() != null) {
            currentSettings.setNewOrder(mailSettingsDao.getGetNewOrderNotifications());
        }
        if (mailSettingsDao.getGetAssignOrderNotifications() != null) {
            currentSettings.setAssignOrder(mailSettingsDao.getGetAssignOrderNotifications());
        }
        if (mailSettingsDao.getGetCancelOrderNotifications() != null) {
            currentSettings.setCancelOrder(mailSettingsDao.getGetCancelOrderNotifications());
        }
        if (mailSettingsDao.getGetCompleteOrderNotifications() != null) {
            currentSettings.setCompleteOrder(mailSettingsDao.getGetCompleteOrderNotifications());
        }
        if (mailSettingsDao.getGetRefuseOrderNotifications() != null) {
            currentSettings.setRefuseOrder(mailSettingsDao.getGetRefuseOrderNotifications());
        }
        return convertToDTO(mailSettingsRepository.updateSettings(currentSettings));
    }

    @Override
    public MailSettingsDao getSettings(Long userId) {
        TpUser user = userDetailsRepository.findUserAccount(userId);
        return convertToDTO(mailSettingsRepository.getSettingsByUser(user));
    }

    private MailSettingsDao convertToDTO(UserMailSettings mailSettings) {
        MailSettingsDao mailSettingsDao = modelMapper.map(mailSettings, MailSettingsDao.class);
        mailSettingsDao.setGetAssignOrderNotifications(mailSettings.getAssignOrder());
        mailSettingsDao.setGetCancelOrderNotifications(mailSettings.getCancelOrder());
        mailSettingsDao.setGetCompleteOrderNotifications(mailSettings.getCompleteOrder());
        mailSettingsDao.setGetRefuseOrderNotifications(mailSettings.getRefuseOrder());
        mailSettingsDao.setGetNewOrderNotifications(mailSettings.getNewOrder());
        return mailSettingsDao;
    }
}
