package com.sap.dao.impl;

import com.sap.dao.DayDao;
import com.sap.models.Day;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class DayDaoImp extends HibernateDaoSupport implements DayDao {

    @Override
    @Transactional
    public void addDay(Day day){
        getHibernateTemplate().save(day);
    }

    @Override
    public List<Day> listDays(int period_id)
    {
        return (List<Day>) getHibernateTemplate().find("from com.sap.models.Day where PERIOD_ID = " + period_id);
    }
    @Override
    public Day getDayById(int id){ return getHibernateTemplate().get(Day.class, id);}


}
