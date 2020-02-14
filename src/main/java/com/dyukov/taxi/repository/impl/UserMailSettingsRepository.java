package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserMailSettings;
import com.dyukov.taxi.exception.UserMailSettingsNotFoundException;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.repository.IUserMailSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class UserMailSettingsRepository implements IUserMailSettingsRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserMailSettingsRepository.class);

    @Autowired
    private EntityManager entityManager;

    @Override
    public UserMailSettings persistNewUserSettings(TpUser user) {
        UserMailSettings settings = new UserMailSettings(user);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateNewOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getUserMailSettings(user);
        settings.setNewOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateCancelOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getUserMailSettings(user);
        settings.setCancelOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateCompleteOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getUserMailSettings(user);
        settings.setCompleteOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateRefuseOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getUserMailSettings(user);
        settings.setRefuseOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateAssignOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getUserMailSettings(user);
        settings.setAssignOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings getSettingsByUser(TpUser user) {
        try {
            String sql = "select e from " + UserMailSettings.class.getName() + " e " +
                    "where e.user.userId = :userId";
            Query query = entityManager.createQuery(sql);
            query.setParameter("userId", user.getUserId());
            return (UserMailSettings) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error("User #" + user.getUserId() + " is not found.", e);
            throw new UserMailSettingsNotFoundException("User #" + user.getUserId() + " is not found.", e);
        }
    }

    @Override
    public UserMailSettings updateSettings(UserMailSettings mailSettings) {
        entityManager.persist(mailSettings);
        entityManager.flush();
        return mailSettings;
    }

    private UserMailSettings getDefaultSettings(TpUser user) {
        return new UserMailSettings(user);
    }

    private UserMailSettings getUserMailSettings(TpUser user) {
        UserMailSettings settings;
        try {
            settings = getSettingsByUser(user);
        } catch (UserMailSettingsNotFoundException e) {
            settings = getDefaultSettings(user);
        }
        return settings;
    }
}
