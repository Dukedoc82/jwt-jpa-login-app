package com.dyukov.taxi.service;

import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public Collection<TpOrderStatus> getAvailableStatuses() {
        return orderStatusRepository.getAvailableStatuses();
    }

    public TpOrderStatus getStatusByKey(String key) {
        return orderStatusRepository.getStatusByKey(key);
    }
}
