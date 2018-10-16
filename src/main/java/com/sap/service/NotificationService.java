package com.sap.service;

import com.sap.models.Notification;

import java.util.List;

public interface NotificationService {

    public void addNotification(Notification n);
    public void removeNotification(Notification n);
    public List<Notification> listNotifications(int team_id);
    public void updateNotification(Notification n);
    public Notification getNotificationById(int id);

}
