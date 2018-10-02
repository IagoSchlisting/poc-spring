package com.sap.dao.impl;

import com.sap.dao.PeriodDao;
import com.sap.models.Period;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PeriodDaoImp extends HibernateDaoSupport implements PeriodDao {

    @Override
    @Transactional
    public void addPeriod(Period period){ getHibernateTemplate().save(period); }

    @Override
    public List<Period> listPeriods(int team_id){
        return (List<Period>) getHibernateTemplate().find("from com.sap.models.Period where TEAM_ID = " + team_id);
    }

    @Override
    @Transactional
    public void removePeriod(int id) {
        Period period = getHibernateTemplate().get(Period.class, id);
        getHibernateTemplate().delete(period);
    }


}
