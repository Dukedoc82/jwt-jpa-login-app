package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TP_TOKEN_BLACKLIST")
public class ExpiredToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp_token_blacklist_seq")
    @SequenceGenerator(sequenceName = "tp_token_blacklist_seq", name = "tp_token_blacklist_seq", allocationSize = 1)
    private Long id;

    @Column
    private String tokenValue;

    @Column
    private Date expireDatetime;

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

    public Date getExpireDatetime() {
        return expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
    }
}
