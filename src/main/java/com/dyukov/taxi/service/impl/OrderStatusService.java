package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.Status;
import com.dyukov.taxi.entity.TpOrderStatus;
import com.dyukov.taxi.repository.IOrderStatusRepository;
import com.dyukov.taxi.service.IOrderStatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class OrderStatusService implements IOrderStatusService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IOrderStatusRepository orderStatusRepository;

    public Collection getAvailableStatuses() {
        return convertToDAO(orderStatusRepository.getAvailableStatuses());
    }

    public Status getStatusByKey(String key) {
        return convertToDAO(orderStatusRepository.getStatusByKey(key));
    }

    private Status convertToDAO(TpOrderStatus status) {
        return modelMapper.map(status, Status.class);
    }

    private Collection convertToDAO(Collection statuses) {
        return (Collection) statuses.stream().map(status -> convertToDAO((TpOrderStatus) status)).collect(Collectors.toList());
    }
}
