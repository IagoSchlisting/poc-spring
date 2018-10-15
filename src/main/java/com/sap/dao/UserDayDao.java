package com.sap.dao;

import com.sap.models.UserDay;

import java.util.List;

public interface UserDayDao {
    public void addUserDay(UserDay userDay);
    public List<UserDay> listUserDays(int day_id);
    public void updateUserDay(UserDay userDay);
    public UserDay getUserDayById(int id);
    public UserDay findUserDay(int user_id, int day_id);
    public List<UserDay> listUserDaysByUser(int user_id);
    public void removeUserDay(UserDay userDay);
}