package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.model.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAllTeams();
    void saveTeam(Team team);
    void updateTeam(Team team);
    void deleteTeam(Team team);
    Team getTeamById(Long id);
}


