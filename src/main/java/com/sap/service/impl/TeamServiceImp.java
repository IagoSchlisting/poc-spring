package com.sap.service.impl;
import com.sap.dao.TeamDao;
import com.sap.service.TeamService;
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
