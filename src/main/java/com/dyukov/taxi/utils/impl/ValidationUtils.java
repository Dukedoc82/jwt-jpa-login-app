package com.dyukov.taxi.utils.impl;

import com.dyukov.taxi.dao.OrderDao;
import com.dyukov.taxi.dao.RegistrationData;
import com.dyukov.taxi.exception.validation.InvalidOrderDataException;
import com.dyukov.taxi.exception.validation.InvalidUserDataException;
import com.dyukov.taxi.utils.IValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtils implements IValidationUtils {

    @Override
    public void validateUser(RegistrationData user) {
        if (user == null) {
            throw new InvalidUserDataException("User data is not specified.");
        }
        validateIsNotEmpty(user.getUserName(), new InvalidUserDataException("Username is not specified or empty."));
        validateIsNotEmpty(user.getFirstName(), new InvalidUserDataException("User first name is not specified or empty."));
        validateIsNotEmpty(user.getLastName(), new InvalidUserDataException("User last name is not specified or empty."));
        validateIsNotEmpty(user.getPassword(), new InvalidUserDataException("User password is not specified or empty."));
        validateIsNotEmpty(user.getPhoneNumber(), new InvalidUserDataException("User phone number is not specified or empty."));
        String phoneNumber = user.getPhoneNumber()
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("-", "")
                .replaceAll(" ", "")
                .replaceAll("\\+", "")
                .trim();
        if (phoneNumber.length() != 11) {
            throw new InvalidUserDataException("Phone number must start with '+7' and contain 10 digits after country code.");
        }
    }

    @Override
    public void validateOrder(OrderDao order) {
        if (order == null) {
            throw new InvalidOrderDataException("Order is null!");
        }
        validateIsNotEmpty(order.getAddressFrom(),
                new InvalidOrderDataException("Order Address From is not specified or null."));
        validateIsNotEmpty(order.getAddressTo(),
                new InvalidOrderDataException("Order Address To is not specified or null."));
        if (order.getClient() == null) {
            throw new InvalidOrderDataException("Order client is not  specified.");
        }
    }

    private void validateIsNotEmpty(String field, RuntimeException exception) {
        if (field == null || field.isEmpty()) {
            throw exception;
        }
    }
}
