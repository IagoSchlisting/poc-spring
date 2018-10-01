package com.sap.models;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
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

    private Date start;

    private Date End;

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return End;
    }

    public void setEnd(Date end) {
        End = end;
    }

    public List<DayPeriod> getDays() {
        return days;
    }

    public void setDays(List<DayPeriod> days) {
        this.days = days;
    }
}
