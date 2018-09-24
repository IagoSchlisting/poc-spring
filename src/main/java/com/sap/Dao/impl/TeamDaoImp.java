package com.sap.Dao.impl;
import com.sap.Dao.TeamDao;
import com.sap.models.Team;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class TeamDaoImp extends HibernateDaoSupport implements TeamDao {

    @Override
    @Transactional
    public void addTeam(Team team) {
        getHibernateTemplate().save(team);
    }

    @Override
    public Team getTeamById(int id) {
        Team team = getHibernateTemplate().get(Team.class, id);
        return team;
    }
}
