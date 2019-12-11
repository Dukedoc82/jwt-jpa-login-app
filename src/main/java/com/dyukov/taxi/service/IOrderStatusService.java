package com.dyukov.taxi.service;

import com.dyukov.taxi.entity.TpOrderStatus;

import java.util.Collection;

public interface IOrderStatusService {

    Collection<TpOrderStatus> getAvailableStatuses();

    TpOrderStatus getStatusByKey(String key);
}
