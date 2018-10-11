package com.sap.models;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "DAY_PERIOD")
@Repository
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DAY_ID")
    private Integer id;

    private LocalDate day;

    // Weekends Or Holidays
    private Boolean special;

    private Integer numberDay;
    private Integer numberLate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERIOD_ID")
    private Period period;

    @OneToMany(mappedBy = "day")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private List<UserDay> memberdays;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public List<UserDay> getMemberdays() {
        return memberdays;
    }

    public void setMemberdays(List<UserDay> memberdays) {
        this.memberdays = memberdays;
    }

    public Integer getNumberDay() {
        return numberDay;
    }

    public void setNumberDay(Integer numberDay) {
        this.numberDay = numberDay;
    }

    public Integer getNumberLate() {
        return numberLate;
    }

    public void setNumberLate(Integer numberLate) {
        this.numberLate = numberLate;
    }
}
