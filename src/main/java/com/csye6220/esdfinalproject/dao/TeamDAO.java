package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.Team;

import java.util.List;

public interface TeamDAO {
    List<Team> getAllTeams();
    void saveTeam(Team team);
    void updateTeam(Team team);
    void deleteTeam(Team team);
    Team getTeamById(Long id);
}
