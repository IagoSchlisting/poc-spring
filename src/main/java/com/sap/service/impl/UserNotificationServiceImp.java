package com.sap.service.impl;

import com.sap.dao.UserNotificationDao;
import com.sap.models.User;
import com.sap.models.UserNotification;
import com.sap.service.UserNotificationService;

import javax.annotation.Resource;
import java.util.List;

public class UserNotificationServiceImp implements UserNotificationService {

    @Resource
    private UserNotificationDao userNotificationDao;

    @Override
    public void addUserNotification(UserNotification un){
        this.userNotificationDao.addUserNotification(un);
    }
    @Override
    public List<UserNotification> listUserNotifications(int user_id){
        return this.userNotificationDao.listUserNotifications(user_id);
    }
}
