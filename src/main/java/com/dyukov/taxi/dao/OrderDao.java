package com.dyukov.taxi.dao;

import java.util.Date;

public class OrderDao {

    private Long id;

    private UserDao client;

    private String addressFrom;

    private String addressTo;

    private String mapCoordinates;

    private Date appointmentDate;

    private String clientComments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDao getClient() {
        return client;
    }

    public void setClient(UserDao client) {
        this.client = client;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getMapCoordinates() {
        return mapCoordinates;
    }

    public void setMapCoordinates(String mapCoordinates) {
        this.mapCoordinates = mapCoordinates;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getClientComments() {
        return clientComments;
    }

    public void setClientComments(String clientComments) {
        this.clientComments = clientComments;
    }

}
