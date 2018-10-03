package com.sap.service;

import com.sap.models.UserDay;

import java.util.List;

public interface UserDayService {
    public void addUserDay(UserDay userDay);
    public List<UserDay> listUserDays(int day_id);
}
