package com.sap.service.impl;

import com.sap.dao.DayDao;
import com.sap.models.Day;
import com.sap.service.DayService;

import javax.annotation.Resource;
import java.util.List;

public class DayServiceImp  implements DayService {

    @Resource
    private DayDao dayDao;

    @Override
    public void addDay(Day day){ this.dayDao.addDay(day); }

    @Override
    public List<Day> listDays(int period_id) {
        return this.dayDao.listDays(period_id);
    }

    @Override
    public Day getDayById(int id){ return this.dayDao.getDayById(id);}

    @Override
    public void updateDay(Day day){
        this.dayDao.updateDay(day);
    }


}
