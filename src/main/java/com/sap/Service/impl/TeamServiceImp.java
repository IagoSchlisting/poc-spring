package com.sap.Service.impl;
import com.sap.Dao.TeamDao;
import com.sap.Service.TeamService;
import com.sap.models.Team;

import javax.annotation.Resource;

public class TeamServiceImp implements TeamService {

    @Resource
    private TeamDao teamDao;

    @Override
    public void addTeam(Team team){
        this.teamDao.addTeam(team);
    }

}
