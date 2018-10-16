package com.sap.dao.impl;

import com.sap.dao.UserNotificationDao;
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
    public List<UserNotification> listUserNotifications(int user_id){
        return (List<UserNotification>) this.getHibernateTemplate().find("from com.sap.models.UserNotification where USER_ID = " + user_id);
    }
}
