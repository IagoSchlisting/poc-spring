package com.sap.service;

import com.sap.models.Team;

import java.util.List;

public interface TeamService {
    public void addTeam(Team team);
    public List<Team> listTeams();
    public Team getTeamById(int id);
}
