package com.sap.service;

import com.sap.models.User;
import com.sap.models.UserNotification;

import java.util.List;

public interface UserNotificationService {

    public void addUserNotification(UserNotification un);
    public List<UserNotification> listUserNotifications(int user_id);
}
