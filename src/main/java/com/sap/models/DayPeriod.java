package com.sap.models;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DAY_PERIOD")
@Repository
public class DayPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DAY_ID")
    private Integer id;

    private Date day;

    private Boolean WeekendOrHoliday;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERIOD_ID")
    private Period period;

    @OneToMany(mappedBy = "day")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private List<MemberPeriodDay> days;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Boolean getWeekendOrHoliday() {
        return WeekendOrHoliday;
    }

    public void setWeekendOrHoliday(Boolean weekendOrHoliday) {
        WeekendOrHoliday = weekendOrHoliday;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<MemberPeriodDay> getDays() {
        return days;
    }

    public void setDays(List<MemberPeriodDay> days) {
        this.days = days;
    }
}
