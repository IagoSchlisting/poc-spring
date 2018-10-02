package com.sap.models;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PERIOD")
@Repository
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PERIOD_ID")
    private Integer id;

    private LocalDate start;
    private LocalDate End;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToMany(mappedBy = "period")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private List<DayPeriod> days;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<DayPeriod> getDays() {
        return days;
    }

    public void setDays(List<DayPeriod> days) {
        this.days = days;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return End;
    }

    public void setEnd(LocalDate end) {
        End = end;
    }
}
