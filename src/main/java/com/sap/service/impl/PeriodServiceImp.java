package com.sap.service.impl;

import com.sap.dao.PeriodDao;
import com.sap.dto.PeriodDTO;
import com.sap.models.Period;
import com.sap.service.PeriodService;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

public class PeriodServiceImp  implements PeriodService {

    @Resource
    private PeriodDao periodDao;

    @Override
    public Period addPeriod(PeriodDTO period){

        List<Period> periods = this.listPeriods(period.getTeam().getId());
        LocalDate start = LocalDate.parse(period.getStartDate());
        LocalDate end = LocalDate.parse(period.getEndDate());

        if(!validatePeriods(periods, start, end)){
            throw  new IllegalArgumentException("Not possible to create a period between this dates, check the corresponding values!");
        }

        Period new_period = convertDTO(period);
        this.periodDao.addPeriod(new_period);
        return new_period;
    }

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

    /**
     * Convert DTO to original type
     * @param period
     * @return original type
     */
    private Period convertDTO(PeriodDTO period){
        Period new_period = new Period();
        new_period.setStart(LocalDate.parse(period.getStartDate()));
        new_period.setEnd(LocalDate.parse(period.getEndDate()));
        new_period.setTeam(period.getTeam());
        return new_period;
    }

    /**
     * Validate if there are no conflicts between each of the period's days
     * @param periods
     * @param start
     * @param end
     * @return Boolean which symbolizes success or error
     */
    private Boolean validatePeriods(List<Period> periods, LocalDate start, LocalDate end){
        if(start.isBefore(LocalDate.now()) || start.isAfter(end)){return false;}
        for(Period period : periods){
            if (start.isEqual(period.getStart())
                    || start.isEqual(period.getEnd())
                    || end.isEqual(period.getEnd())
                    || end.isEqual(period.getStart())
                    || (start.isAfter(period.getStart()) && start.isBefore(period.getEnd()))
                    || (start.isBefore(period.getStart()) && end.isAfter(period.getStart()))
            ){
                return false;
            }
        }
        return true;
    }

}
