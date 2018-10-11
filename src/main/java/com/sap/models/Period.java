package com.sap.models;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
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
    private List<Day> days;

    private Integer numberDayNormal;
    private Integer numberLateNormal;
    private Integer numberDaySpecial;
    private Integer numberLateSpecial;

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

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
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

    public Integer getNumberDayNormal() {
        return numberDayNormal;
    }

    public void setNumberDayNormal(Integer numberDayNormal) {
        this.numberDayNormal = numberDayNormal;
    }

    public Integer getNumberLateNormal() {
        return numberLateNormal;
    }

    public void setNumberLateNormal(Integer numberLateNormal) {
        this.numberLateNormal = numberLateNormal;
    }

    public Integer getNumberDaySpecial() {
        return numberDaySpecial;
    }

    public void setNumberDaySpecial(Integer numberDaySpecial) {
        this.numberDaySpecial = numberDaySpecial;
    }

    public Integer getNumberLateSpecial() {
        return numberLateSpecial;
    }

    public void setNumberLateSpecial(Integer numberLateSpecial) {
        this.numberLateSpecial = numberLateSpecial;
    }
}
