package com.sap.service.impl;

import com.sap.dao.UserDayDao;
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
    public void updateUserDay(UserDay userDay){
        this.userDayDao.updateUserDay(userDay);
    }
    @Override
    public UserDay getUserDayById(int id){
        return this.userDayDao.getUserDayById(id);
    }
}
