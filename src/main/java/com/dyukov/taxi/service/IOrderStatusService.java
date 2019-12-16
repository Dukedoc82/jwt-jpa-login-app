package com.dyukov.taxi.service;

import com.dyukov.taxi.dao.Status;

import java.util.Collection;

public interface IOrderStatusService {

    Collection<Status> getAvailableStatuses();

    Status getStatusByKey(String key);
}
