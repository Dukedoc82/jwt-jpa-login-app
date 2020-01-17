package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.service.context.ContextAction;

import java.util.Collection;

public interface IMailService {

    void prepareAndSend(String recipient, String message);

    void sendRegistrationConfirmationEmail(String recipient, String confirmToken);

    void sendOrderUpdateNotification(Collection<String> recipients, OrderDetailsDao order, ContextAction action);
}
