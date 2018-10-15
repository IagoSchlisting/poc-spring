package com.sap.service.impl;

import com.sap.dao.DayDao;
import com.sap.dto.DayDTO;
import com.sap.models.Day;
import com.sap.models.User;
import com.sap.service.DayService;
import com.sap.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.util.List;

public class DayServiceImp  implements DayService {

    @Resource
    private DayDao dayDao;
    @Resource
    private UserService userService;

    @Override
    public void addDay(Day day){ this.dayDao.addDay(day); }

    @Override
    public List<Day> listDays(int period_id) {
        return this.dayDao.listDays(period_id);
    }

    @Override
    public Day getDayById(int id){ return this.dayDao.getDayById(id);}

    @Override
    public void updateDay(DayDTO day){
        Day updatedDay = this.getDayById(day.getId());
        updatedDay.setSpecial(day.getSpecial());
        this.dayDao.updateDay(updatedDay);
    }

    @Override
    public void updateDay(Day day){
        if(day.getNumberDay() < 0 || day.getNumberLate() < 0){
            throw new IllegalArgumentException("Not possible to update day with negative param values!");
        }

        //REFACTORING NEEDED AFTER
        User principal = this.userService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        int totalMembers = this.userService.listUsers(day.getPeriod().getTeam().getId(), principal.getId()).size();

        if(!day.getSpecial()){
            if(totalMembers != day.getNumberDay() + day.getNumberLate()){
                throw  new IllegalArgumentException("Number of members per shift must correspond to the following equation: DAY + LATE = (total members)");
            }
        }else{
            if(day.getNumberDay() + day.getNumberLate() > totalMembers){
                throw  new IllegalArgumentException("Sum of total members required for shift cannot be higher than the number of total members.");
            }
        }
        this.dayDao.updateDay(day);
    }

     /**
     * Verify if user has authorization to execute action
     * @param id
     * @return boolean
     */
    public Boolean notAuthorized(int id) {
        User principal = this.userService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Day day = this.getDayById(id);
        if (principal.getTeam().getId() != day.getPeriod().getTeam().getId()) {
            return true;
        }
        return false;
    }

}
