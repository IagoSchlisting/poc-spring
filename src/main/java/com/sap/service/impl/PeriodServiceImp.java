package com.sap.service.impl;

import com.sap.dao.PeriodDao;
import com.sap.models.Period;
import com.sap.service.PeriodService;

import javax.annotation.Resource;
import java.util.List;

public class PeriodServiceImp  implements PeriodService {

    @Resource
    private PeriodDao periodDao;

    @Override
    public void addPeriod(Period period){ this.periodDao.addPeriod(period); }

    @Override
    public List<Period> listPeriods(int team_id) {
        return this.periodDao.listPeriods(team_id);
    }

    @Override
    public void removePeriod(int id) {
        this.periodDao.removePeriod(id);
    }

    @Override
    public Period getPeriodById(int id){
        return this.periodDao.getPeriodById(id);
    };

}
