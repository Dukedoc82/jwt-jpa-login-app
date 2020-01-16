package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDetailsDao;

import java.util.Collection;

public interface IMailService {

    void prepareAndSend(String recipient, String message);

    void sendNewOrderNotification(String recipient, OrderDetailsDao order);

    void sendCancelOrderNotification(Collection<String> recipients, OrderDetailsDao order);

    void sendRegistrationConfirmationEmail(String recipient, String confirmToken);
}
