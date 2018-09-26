package com.sap.service.impl;
import com.sap.dao.TeamDao;
import com.sap.service.TeamService;
import com.sap.models.Team;

import javax.annotation.Resource;
import java.util.List;

public class TeamServiceImp implements TeamService {

    @Resource
    private TeamDao teamDao;

    @Override
    public void addTeam(Team team){
        this.teamDao.addTeam(team);
    }

    @Override
    public List<Team> listTeams(){
        return this.teamDao.listTeams();
    }

    @Override
    public Team getTeamById(int id){
        return this.teamDao.getTeamById(id);
    }

}
