package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.MailSettingsDao;

public interface IMailSettingsService {

    MailSettingsDao updateSettings(Long userId, MailSettingsDao mailSettingsDao);
}
