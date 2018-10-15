package com.sap.service.impl;

import com.sap.dao.UserDayDao;
import com.sap.dto.UserDayDTO;
import com.sap.models.Shift;
import com.sap.models.UserDay;
import com.sap.service.UserDayService;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public List<UserDay> listUserDaysByUser(int user_id){return this.userDayDao.listUserDaysByUser(user_id);}

    @Override
    public UserDay updateUserDay(UserDayDTO userDay){

        if(!userDay.getDisponibility()){
            userDay.setShift("none");
        }

        UserDay updated_userDay = this.getUserDayById(userDay.getId());
        switch (userDay.getShift()){
            case "day":
                updated_userDay.setShift(Shift.DAY);
                updated_userDay.setAnyShift(false);
                break;
            case "late":
                updated_userDay.setShift(Shift.LATE);
                updated_userDay.setAnyShift(false);
                break;
            case "none":
                updated_userDay.setShift(Shift.NONE);
                updated_userDay.setAnyShift(false);
                break;
            default:
                updated_userDay.setShift(Shift.DAY);
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

}
