package com.sap.models;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Repository;

import javax.enterprise.inject.Default;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.EnumMap;

@Entity
@Table(name = "MEMBER_DAY")
@Repository
public class MemberPeriodDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_DAY_ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DAY_ID")
    private DayPeriod day;

    @Enumerated(EnumType.ORDINAL)
    private Shift shift;

    @Column(columnDefinition = "Boolean default true")
    private Boolean disponibility;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DayPeriod getDay() {
        return day;
    }

    public void setDay(DayPeriod day) {
        this.day = day;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Boolean getDisponibility() {
        return disponibility;
    }

    public void setDisponibility(Boolean disponibility) {
        this.disponibility = disponibility;
    }
}
