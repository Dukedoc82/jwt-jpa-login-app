package com.dyukov.taxi.exception;

public abstract class TaxiServiceException extends RuntimeException {

    private static final long serialVersionUID = -2200022942566725295L;

    public static final String ORDER_DOES_NOT_EXIST = "Order #%d doesn't exist.";
    public static final String ORDER_IS_ALREADY_ASSIGNED = "Order #%d is already assigned to specified driver.";
    public static final String ORDER_IS_ALREADY_COMPLETED = "Order #%d is already completed.";
    public static final String ORDER_IS_NOT_ASSIGNED = "Order #%d is not assigned.";
    public static final String ORDER_IS_NOT_ASSIGNED_TO_DRIVER = "Order #%d is not assigned to you.";
    public static final String STATUS_DOES_NOT_EXIST = "Status '%s' doesn't exist.";
    public static final String USER_NAME_DOES_NOT_EXIST = "User #%s doesn't exist.";
    public static final String WRONG_ASSIGNMENT_ORDER_STATUS =
            "Cannot assign cancelled or completed order. Order #%d is %s.";
    public static final String WRONG_CANCELLATION_ORDER_STATUS =
            "Cannot cancel cancelled or assigned order. Order #%d is %s.";
    static final String USER_ID_DOES_NOT_EXIST = "User #%d doesn't exist.";

}
