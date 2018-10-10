package com.sap.service;

import com.sap.dto.DayDTO;
import com.sap.models.Day;

import java.util.List;

public interface DayService {
    public void addDay(Day day);
    public List<Day> listDays(int period_id);
    public Day getDayById(int id);
    public void updateDay(DayDTO day);
    public Boolean notAuthorized(int id);
}
