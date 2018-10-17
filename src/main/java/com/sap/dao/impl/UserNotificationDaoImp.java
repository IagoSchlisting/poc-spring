package com.sap.dao.impl;

import com.sap.dao.UserNotificationDao;
import com.sap.models.User;
import com.sap.models.UserNotification;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserNotificationDaoImp extends HibernateDaoSupport implements UserNotificationDao {

    @Override
    @Transactional
    public void addUserNotification(UserNotification un){
        this.getHibernateTemplate().save(un);
    }

    @Override
    public List<UserNotification> listUserNotifications(int user_id){
        return (List<UserNotification>) this.getHibernateTemplate().find("from com.sap.models.UserNotification where USER_ID = " + user_id);
    }

    @Override
    public UserNotification getUserNotification(int notification_id, int user_id){
        String query = "from com.sap.models.UserNotification where USER_ID = " + user_id + " and NOTIFICATION_ID = " + notification_id;
        List<UserNotification> userNotifications = (List<UserNotification>) this.getHibernateTemplate().find(query);
        return userNotifications.get(0);
    }

    @Override
    @Transactional
    public void updateUserNotification(UserNotification un){
        this.getHibernateTemplate().update(un);
    }
}
