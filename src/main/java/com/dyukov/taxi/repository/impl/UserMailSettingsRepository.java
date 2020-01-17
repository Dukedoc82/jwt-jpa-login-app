package com.dyukov.taxi.repository.impl;

import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserMailSettings;
import com.dyukov.taxi.exception.UserNotFoundException;
import com.dyukov.taxi.repository.IUserMailSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class UserMailSettingsRepository implements IUserMailSettingsRepository {

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
        UserMailSettings settings = getSettingsByUser(user);
        settings.setNewOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateCancelOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getSettingsByUser(user);
        settings.setCancelOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateCompleteOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getSettingsByUser(user);
        settings.setCompleteOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateRefuseOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getSettingsByUser(user);
        settings.setRefuseOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings updateAssignOrderNotification(TpUser user, boolean receive) {
        UserMailSettings settings = getSettingsByUser(user);
        settings.setAssignOrder(receive);
        entityManager.persist(settings);
        entityManager.flush();
        return settings;
    }

    @Override
    public UserMailSettings getSettingsByUser(TpUser user) {
        try {
            String sql = "select e from " + UserMailSettings.class.getName() + " e " +
                    "where e.user = :user";
            Query query = entityManager.createQuery(sql);
            query.setParameter("user", user);
            return (UserMailSettings) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException("User #" + user.getUserId() + " is not found.", e);
        }
    }

    @Override
    public UserMailSettings updateSettings(UserMailSettings mailSettings) {
        entityManager.persist(mailSettings);
        entityManager.flush();
        return mailSettings;
    }
}
