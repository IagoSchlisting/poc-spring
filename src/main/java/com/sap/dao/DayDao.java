package com.sap.dao;

import com.sap.models.Day;

import java.util.List;

public interface DayDao {
    public void addDay(Day day);
    public List<Day> listDays(int period_id);
    public Day getDayById(int id);
}
