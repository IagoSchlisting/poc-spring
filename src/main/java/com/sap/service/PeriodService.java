package com.sap.service;

import com.sap.models.Period;

import java.util.List;

public interface PeriodService {
    public void addPeriod(Period period);
    public List<Period> listPeriods();
    public void removePeriod(int id);
}
