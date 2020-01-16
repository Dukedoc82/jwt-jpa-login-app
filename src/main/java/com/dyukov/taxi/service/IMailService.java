package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDetailsDao;

public interface IMailService {

    void prepareAndSend(String recipient, String message);

    void sendNewOrderNotification(String recipient, OrderDetailsDao order);

    void sendRegistrationConfirmationEmail(String recipient, String confirmToken);
}
