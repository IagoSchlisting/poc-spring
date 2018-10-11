package com.sap.dao;

import com.sap.models.Team;

import java.util.List;

public interface TeamDao {
    public void addTeam(Team team);
    public List<Team> listTeams();
    public Team getTeamById(int id);
    public void removeTeam(Team team);
}
