package com.dyukov.taxi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Tp_Status",
        uniqueConstraints = { //
                @UniqueConstraint(name = "Tp_Order_UK", columnNames = "Title_Key") })
public class TpOrderStatus implements Serializable {

    private static final long serialVersionUID = -8696341617153791138L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tp_status_seq")
    @SequenceGenerator(name = "tp_status_seq", sequenceName = "tp_status_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title_key")
    private String titleKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    @Override
    public String toString() {
        return "TpOrderStatus{" +
                "id=" + id +
                ", titleKey='" + titleKey + '\'' +
                '}';
    }
}
