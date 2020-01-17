package com.dyukov.taxi.utils;

import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.exception.validation.InvalidUserDataException;

public interface IValidationUtils {

    void validateUser(RegistrationData user);

    void validateOrder(OrderDao order);
}
