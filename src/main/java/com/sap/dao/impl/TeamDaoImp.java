package com.sap.dao.impl;
import com.sap.dao.TeamDao;
import com.sap.models.Team;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class TeamDaoImp extends HibernateDaoSupport implements TeamDao {

    @Override
    @Transactional
    public void addTeam(Team team) {
        getHibernateTemplate().save(team);
    }
}
