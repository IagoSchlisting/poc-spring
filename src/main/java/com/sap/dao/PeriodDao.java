package com.sap.dao;

import com.sap.models.Period;

import java.util.List;

public interface PeriodDao {
    public void addPeriod(Period period);
    public List<Period> listPeriods();
    public void removePeriod(int id);
}

