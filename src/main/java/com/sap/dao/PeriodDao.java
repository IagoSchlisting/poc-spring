package com.sap.dao;

import com.sap.models.Period;

import java.util.List;

public interface PeriodDao {
    public void addPeriod(Period period);
    public List<Period> listPeriods(int team_id);
    public void removePeriod(int id);
    public Period getPeriodById(int id);
}

