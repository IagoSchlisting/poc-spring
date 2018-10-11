package com.sap.dao.impl;
import com.sap.dao.TeamDao;
import com.sap.models.Team;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TeamDaoImp extends HibernateDaoSupport implements TeamDao {

    @Override
    @Transactional
    public void addTeam(Team team) {
        getHibernateTemplate().save(team);
    }

    @Override
    public List<Team> listTeams(){
        return (List<Team>) getHibernateTemplate().find("from com.sap.models.Team");
    }

    @Override
    public Team getTeamById(int id){
        return getHibernateTemplate().get(Team.class, id);
    }

    @Override
    public void removeTeam(Team team){
        this.getHibernateTemplate().delete(team);
    }
}
