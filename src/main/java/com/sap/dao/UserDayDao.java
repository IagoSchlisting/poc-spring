package com.sap.dao;

import com.sap.models.UserDay;

import java.util.List;

public interface UserDayDao {
    public void addUserDay(UserDay userDay);
    public List<UserDay> listUserDays(int day_id);
}
