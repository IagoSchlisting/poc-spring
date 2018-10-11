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
