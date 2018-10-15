package com.sap.dao.impl;

import com.sap.dao.UserDayDao;
import com.sap.models.Day;
import com.sap.models.UserDay;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.AssertTrue;
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

    public List<UserDay> listUserDaysByUser(int user_id){
        return (List<UserDay>) this.getHibernateTemplate().find("from com.sap.models.UserDay where USER_ID = " + user_id);
    }

    @Override
    @Transactional
    public void removeUserDay(UserDay userDay){
        this.getHibernateTemplate().delete(userDay);
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
    @Override
    public UserDay findUserDay(int user_id, int day_id){
        List<UserDay> userDays;
        String query = "from com.sap.models.UserDay where USER_ID = " + user_id + "and DAY_ID = " + day_id;
        userDays = (List<UserDay>) getHibernateTemplate().find(query);
        UserDay userDay;
        if(userDays.isEmpty()){ userDay = new UserDay();}
        else{userDay = userDays.get(0);}
        return userDay;
    }
}
