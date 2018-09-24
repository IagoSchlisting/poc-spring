package com.sap.Service;

import com.sap.models.Team;

public interface TeamService {
    public void addTeam(Team team);
    public Team getTeamById(int id);
}
