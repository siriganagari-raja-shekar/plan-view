package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.dao.TeamDAO;
import com.csye6220.esdfinalproject.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDAO teamDAO;

    @Override
    public List<Team> getAllTeams() {
        return teamDAO.getAllTeams();
    }

    @Override
    public void saveTeam(Team team) {
        teamDAO.saveTeam(team);
    }

    @Override
    public void updateTeam(Team team) {
        teamDAO.updateTeam(team);
    }

    @Override
    public void deleteTeam(Team team) {
        teamDAO.deleteTeam(team);
    }

    @Override
    public Team getTeamById(Long id) {
        return teamDAO.getTeamById(id);
    }
}
