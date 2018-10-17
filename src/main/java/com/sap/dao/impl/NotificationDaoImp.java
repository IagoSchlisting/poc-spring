package com.sap.dao.impl;

import com.sap.dao.NotificationDao;
import com.sap.models.Notification;
import com.sap.models.UserNotification;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class NotificationDaoImp extends HibernateDaoSupport implements NotificationDao {

    @Override
    @Transactional
    public void addNotification(Notification n){
        this.getHibernateTemplate().save(n);
    }

    @Override
    @Transactional
    public void removeNotification(Notification n){
        this.getHibernateTemplate().delete(n);
    }

    @Override
    public List<Notification> listNotifications(int team_id){
        return (List<Notification>) this.getHibernateTemplate().find("from com.sap.models.Notification where TEAM_ID = " + team_id);
    }

    @Override
    public Notification getNotificationById(int id){
        return this.getHibernateTemplate().get(Notification.class, id);
    }

    @Override
    public List<Notification> listNotificationsForUser(int user_id){

        String query = "select n from com.sap.models.Notification as n inner join n.userNotifications un on n.id = un.notification.id" +
                " where un.user.id = " + user_id + " and un.visualized = 0";
        return (List<Notification>) this.getHibernateTemplate().find(query);
    }

}
