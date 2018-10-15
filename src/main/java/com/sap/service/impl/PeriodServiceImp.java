package com.sap.service.impl;

import com.sap.dao.PeriodDao;
import com.sap.dto.PeriodDTO;
import com.sap.models.Period;
import com.sap.models.User;
import com.sap.service.PeriodService;
import com.sap.service.TeamService;
import com.sap.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

public class PeriodServiceImp  implements PeriodService {

    @Resource
    private PeriodDao periodDao;
    @Resource
    private UserService userService;

    @Override
    public Period addPeriod(PeriodDTO period){

        List<Period> periods = this.listPeriods(period.getTeam().getId());
        LocalDate start = LocalDate.parse(period.getStartDate());
        LocalDate end = LocalDate.parse(period.getEndDate());

        if(period.getNumberDayNormal() < 0 || period.getNumberDaySpecial() < 0 || period.getNumberLateNormal() < 0 || period.getNumberLateSpecial() < 0){
            throw  new IllegalArgumentException("Not possible to create period with negative param values!");
        }
        //REFACTORING NEEDED AFTER
        User principal = this.userService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        int totalMembers = this.userService.listUsers(period.getTeam().getId(), principal.getId()).size();

        if(totalMembers != period.getNumberDayNormal() + period.getNumberLateNormal()){
            throw  new IllegalArgumentException("Number of members per shift must correspond to the following equation: DAY + LATE = (total members)");
        }

        if(period.getNumberLateSpecial() + period.getNumberDaySpecial() > totalMembers){
            throw  new IllegalArgumentException("Sum of total members required for shift cannot be higher than the number of total members.");
        }

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

        new_period.setNumberDayNormal(period.getNumberDayNormal());
        new_period.setNumberDaySpecial(period.getNumberDaySpecial());
        new_period.setNumberLateNormal(period.getNumberLateNormal());
        new_period.setNumberLateSpecial(period.getNumberLateSpecial());

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

     /** Verify if user has authorization to execute action
     * @param id
     * @return boolean
     */
    public Boolean notAuthorized(int id) {
        User principal = this.userService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Period period = this.getPeriodById(id);
        if (principal.getTeam().getId() != period.getTeam().getId()) {
            return true;
        }
        return false;
    }

}
