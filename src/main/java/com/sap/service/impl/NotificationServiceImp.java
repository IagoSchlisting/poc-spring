package com.sap.service.impl;

import com.sap.dao.NotificationDao;
import com.sap.models.Notification;
import com.sap.service.NotificationService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class NotificationServiceImp implements NotificationService {

    @Resource
    private NotificationDao notificationDao;

    @Override
    public void addNotification(Notification n){
        this.notificationDao.addNotification(n);
    }

    @Override
    public void removeNotification(Notification n){
        this.notificationDao.removeNotification(n);
    }

    @Override
    public List<Notification> listNotifications(int team_id){
        return this.notificationDao.listNotifications(team_id);
    }

    @Override
    public void updateNotification(Notification n){
        this.notificationDao.updateNotification(n);
    }

    @Override
    public Notification getNotificationById(int id){
        return this.notificationDao.getNotificationById(id);
    }

}
