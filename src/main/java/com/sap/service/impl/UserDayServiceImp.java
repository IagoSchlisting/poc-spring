package com.sap.service.impl;

import com.sap.dao.UserDayDao;
import com.sap.dto.UserDayDTO;
import com.sap.models.Day;
import com.sap.models.Shift;
import com.sap.models.User;
import com.sap.models.UserDay;
import com.sap.service.UserDayService;
import com.sap.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

public class UserDayServiceImp implements UserDayService {

    @Resource
    private UserDayDao userDayDao;
    @Resource
    private UserService userService;

    @Override
    public void addUserDay(UserDay userDay){
        this.userDayDao.addUserDay(userDay);
    }
    @Override
    public List<UserDay> listUserDays(int day_id){
        return this.userDayDao.listUserDays(day_id);
    }

    @Override
    public List<UserDay> listUserDaysByUser(int user_id){return this.userDayDao.listUserDaysByUser(user_id);}

    @Override
    public UserDay updateUserDay(UserDayDTO userDay){

        if(!userDay.getDisponibility()){
            userDay.setShift("none");
        }

        UserDay updated_userDay = this.getUserDayById(userDay.getId());
        switch (userDay.getShift()){
            case "day":
                if(canSelectShift(userDay, Shift.DAY)){
                    updated_userDay.setShift(Shift.DAY);
                    updated_userDay.setAnyShift(false);
                }else{
                    throw new IllegalArgumentException("Not possible to bound user to the shift, number required already fulfilled.");
                }
                break;
            case "late":
                if(canSelectShift(userDay, Shift.LATE)){
                    updated_userDay.setShift(Shift.LATE);
                    updated_userDay.setAnyShift(false);
                }else{
                    throw new IllegalArgumentException("Not possible to bound user to the shift, number required already fulfilled.");
                }
                break;
            case "none":
                updated_userDay.setShift(Shift.NONE);
                updated_userDay.setAnyShift(false);
                break;
            default:
                updated_userDay.setShift(getNeededShift(updated_userDay.getDay()));
                updated_userDay.setAnyShift(true);
                break;
        }
        updated_userDay.setDisponibility(userDay.getDisponibility());
        this.userDayDao.updateUserDay(updated_userDay);
        return updated_userDay;
    }
    @Override
    public UserDay getUserDayById(int id){
        return this.userDayDao.getUserDayById(id);
    }
    @Override
    public UserDay findUserDay(int user_id, int day_id){
        return this.userDayDao.findUserDay(user_id, day_id);
    }
    @Override
    public void removeUserDay(UserDay userDay){
        this.userDayDao.removeUserDay(userDay);
    }


    private Boolean canSelectShift(UserDayDTO userDay, Shift shift){
        List<UserDay> memberDays = this.listUserDays(this.getUserDayById(userDay.getId()).getDay().getId());
        for (UserDay memberDay : memberDays) {
            if(memberDay.getShift() == shift && memberDay.getAnyShift()){
                UserDay updated_memberDay = this.getUserDayById(memberDay.getId());
                updated_memberDay.setShift(memberDay.getShift() == Shift.DAY ? Shift.LATE : Shift.DAY);
                this.userDayDao.updateUserDay(updated_memberDay);
                return true;
            }

        }
        return false;
    }


    private Integer availableOnDay(Day day){
        int requireDay = day.getNumberDay();
        int totalDay = 0;
        List<UserDay> memberDays = this.listUserDays(day.getId());
        for (UserDay memberDay : memberDays) {
            if (memberDay.getShift() == Shift.DAY) {
                totalDay++;
            }
        }
        return requireDay - totalDay;
    }

    private Integer availableOnLate(Day day){
        int requireLate = day.getNumberLate();
        int totalLate = 0;
        List<UserDay> memberDays = this.listUserDays(day.getId());
        for (UserDay memberDay : memberDays) {
            if((memberDay.getShift() == Shift.LATE)) {
                totalLate++;
            }
        }
        return requireLate - totalLate;
    }

    @Override
    public Shift getNeededShift(Day day){
        // Verifies which shift needs more members
        if (this.availableOnDay(day) > this.availableOnLate(day)) {
            return Shift.DAY;
        } else if (this.availableOnDay(day) < this.availableOnLate(day)) {
            return Shift.LATE;
        } else if (this.availableOnDay(day) == 0 && this.availableOnLate(day) == 0) {
            throw new IllegalArgumentException("Not possible to bound user to the day, number required already fulfilled.");
        } else {
            return Shift.DAY;
        }
    }

}
