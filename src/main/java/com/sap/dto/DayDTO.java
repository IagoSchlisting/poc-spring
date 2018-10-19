package com.sap.dto;

import com.sap.models.Day;
import com.sap.models.UserDay;

import java.time.LocalDate;
import java.util.List;

public class DayDTO {

    private Integer id;

    private Boolean Special;

    private LocalDate day;
    private Integer numberDay;
    private Integer numberLate;
    private List<UserDay> memberdays;

    private Boolean completed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSpecial() {
        return Special;
    }

    public void setSpecial(Boolean special) {
        Special = special;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
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

    public List<UserDay> getMemberdays() {
        return memberdays;
    }

    public void setMemberdays(List<UserDay> memberdays) {
        this.memberdays = memberdays;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
