package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.OrderDetailsDao;

public interface IMailService {

    public void prepareAndSend(String recipient, String message);

    public void sendNewOrderNotification(String recipient, OrderDetailsDao order);
}
