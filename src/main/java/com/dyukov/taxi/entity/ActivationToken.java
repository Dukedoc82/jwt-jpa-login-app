package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "TP_ACTIVATION_TOKEN")
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tokenValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TpUser user;

    @Column
    private Date expirationDateTime;

    public ActivationToken() {
        super();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        expirationDateTime = cal.getTime();
    }

    public ActivationToken(String tokenValue, TpUser user) {
        this();
        this.tokenValue = tokenValue;
        this.user = user;
    }

    public ActivationToken(Long id, String tokenValue, TpUser user) {
        this(tokenValue, user);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public TpUser getUser() {
        return user;
    }

    public void setUser(TpUser user) {
        this.user = user;
    }

    public Date getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(Date expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }
}
