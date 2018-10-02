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
    public List<Period> listPeriods() {
        return this.periodDao.listPeriods();
    }

    @Override
    public void removePeriod(int id) {
        this.periodDao.removePeriod(id);
    }

}
