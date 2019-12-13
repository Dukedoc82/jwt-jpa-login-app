package com.dyukov.taxi.repository;

import com.dyukov.taxi.entity.TpOrderStatus;

import java.util.Collection;

public interface IOrderStatusRepository {

    TpOrderStatus getStatusByKey(String key);

    Collection getAvailableStatuses();
}
