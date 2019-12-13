package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import com.dyukov.taxi.service.IOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderStatusService implements IOrderStatusService {

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    public Collection<TpOrderStatus> getAvailableStatuses() {
        return orderStatusRepository.getAvailableStatuses();
    }

    public TpOrderStatus getStatusByKey(String key) {
        return orderStatusRepository.getStatusByKey(key);
    }
}
