package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserMailSettings;

public interface IUserMailSettingsRepository {

    UserMailSettings persistNewUserSettings(TpUser user);

    UserMailSettings updateNewOrderNotification(TpUser user, boolean receive);

    UserMailSettings updateCancelOrderNotification(TpUser user, boolean receive);

    UserMailSettings updateCompleteOrderNotification(TpUser user, boolean receive);

    UserMailSettings updateRefuseOrderNotification(TpUser user, boolean receive);

    UserMailSettings updateAssignOrderNotification(TpUser user, boolean receive);

    UserMailSettings getSettingsByUser(TpUser user);

    UserMailSettings updateSettings(UserMailSettings mailSettings);
}
