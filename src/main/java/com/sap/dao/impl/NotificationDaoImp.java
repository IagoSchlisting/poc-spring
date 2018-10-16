package com.sap.dao.impl;

import com.sap.dao.NotificationDao;
import com.sap.models.Notification;
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

    public List<Notification> listNotifications(int team_id){
        return (List<Notification>) this.getHibernateTemplate().find("from com.sap.models.Notification where TEAM_ID = " + team_id);
    }

    public void updateNotification(Notification n){
        this.getHibernateTemplate().update(n);
    }

    public Notification getNotificationById(int id){
        return this.getHibernateTemplate().get(Notification.class, id);
    }

}
