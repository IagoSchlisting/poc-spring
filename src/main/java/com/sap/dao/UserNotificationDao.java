package com.sap.dao;

import com.sap.models.User;
import com.sap.models.UserNotification;

import java.util.List;

public interface UserNotificationDao {
    public void addUserNotification(UserNotification un);
    public UserNotification getUserNotification(int notification_id, int user_id);
    public void updateUserNotification(UserNotification un);
    public List<UserNotification> listUserNotifications(int user_id);
}
