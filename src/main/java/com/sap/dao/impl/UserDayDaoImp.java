package com.sap.dao.impl;

import com.sap.dao.UserDayDao;
import com.sap.models.UserDay;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserDayDaoImp extends HibernateDaoSupport implements UserDayDao {

    @Override
    @Transactional
    public void addUserDay(UserDay userDay){
        this.getHibernateTemplate().save(userDay);
    }
    @Override
    public List<UserDay> listUserDays(int day_id){
        return (List<UserDay>) this.getHibernateTemplate().find("from com.sap.models.UserDay where DAY_ID = " + day_id);
    }
    @Override
    @Transactional
    public void updateUserDay(UserDay userDay){
        this.getHibernateTemplate().update(userDay);
    }
    @Override
    public UserDay getUserDayById(int id){
        return this.getHibernateTemplate().get(UserDay.class, id);
    }
}
