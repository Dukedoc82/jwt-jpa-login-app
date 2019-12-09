package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Tp_Order")
public class TpOrder implements Serializable {

    private static final long serialVersionUID = -5272882102371005414L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Client_Id")
    private TpUser client;

    @Column(name = "Address_From", length = 600, nullable = false)
    private String addressFrom;

    @Column(name = "Address_To", length = 600, nullable = false)
    private String addressTo;

    @Column(name = "Map_Coordinates", length = 30)
    private String mapCoordinates;

    @Column(name = "APPOINTMENT_DATETIME", nullable = false)
    private Date appointmentDate;

    @Column(name = "CLIENT_COMMENTS")
    private String clientComments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return client.getUserName();
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

    public Long getClientId() {
        return client.getUserId();
    }

    public void setClient(TpUser client) {
        this.client = client;
    }

    public TpUser getClient() {
        return client;
    }
}
