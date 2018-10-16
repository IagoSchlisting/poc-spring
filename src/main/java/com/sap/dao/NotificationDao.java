package com.sap.dao;

import com.sap.models.Notification;

import java.util.List;

public interface NotificationDao {

    public void addNotification(Notification n);
    public void removeNotification(Notification n);
    public List<Notification> listNotifications(int team_id);
    public void updateNotification(Notification n);
    public Notification getNotificationById(int id);

}
