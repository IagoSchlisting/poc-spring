package com.sap.dao;

import com.sap.models.Notification;

import java.util.List;

public interface NotificationDao {

    public void addNotification(Notification n);
    public void removeNotification(Notification n);
    public List<Notification> listNotifications(int team_id);
    public Notification getNotificationById(int id);
    public List<Notification> listNotificationsForUser(int user_id);

}
