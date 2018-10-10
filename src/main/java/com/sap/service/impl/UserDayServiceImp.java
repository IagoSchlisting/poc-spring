package com.sap.service.impl;

import com.sap.dao.UserDayDao;
import com.sap.dto.UserDayDTO;
import com.sap.models.Shift;
import com.sap.models.UserDay;
import com.sap.service.UserDayService;

import javax.annotation.Resource;
import java.util.List;

public class UserDayServiceImp implements UserDayService {

    @Resource
    private UserDayDao userDayDao;

    @Override
    public void addUserDay(UserDay userDay){
        this.userDayDao.addUserDay(userDay);
    }
    @Override
    public List<UserDay> listUserDays(int day_id){
        return this.userDayDao.listUserDays(day_id);
    }
    @Override
    public UserDay updateUserDay(UserDayDTO userDay){
        UserDay updated_userDay = this.getUserDayById(userDay.getId());
        switch (userDay.getShift()){
            case "day":
                updated_userDay.setShift(Shift.DAY);
                break;
            case "late":
                updated_userDay.setShift(Shift.LATE);
                break;
            default:
                updated_userDay.setShift(Shift.ANY);
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

}
