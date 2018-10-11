package com.sap.dto;

import com.sap.models.Team;

public class PeriodDTO {

    private String startDate;

    private String endDate;

    private Team team;

    private Integer numberDayNormal;
    private Integer numberLateNormal;
    private Integer numberDaySpecial;
    private Integer numberLateSpecial;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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
