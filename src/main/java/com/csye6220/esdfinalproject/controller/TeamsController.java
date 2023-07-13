package com.csye6220.esdfinalproject.controller;

import com.csye6220.esdfinalproject.model.Team;
import com.csye6220.esdfinalproject.model.User;
import com.csye6220.esdfinalproject.model.UserRole;
import com.csye6220.esdfinalproject.service.TeamService;
import com.csye6220.esdfinalproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TeamsController {

    @Autowired
    private UserService userService;

    @Autowired
    TeamService teamService;

    @GetMapping("/teams/create")
    public ModelAndView navigateToCreateTeam(HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");

        if(!user.getRole().equals(UserRole.ADMIN)){
            return new ModelAndView("forbidden");
        }

        List<User> allUsers = userService.getAllUsers();

        return new ModelAndView("create_team", "allUsers", allUsers);
    }

    @PostMapping("/teams/create")
    public String createNewTeam(
            HttpServletRequest request,
            @RequestParam(name = "team-name") String teamName,
            @RequestParam(name = "team-members") String teamMembers
    ){

        User user = (User)request.getSession().getAttribute("user");

        if(!user.getRole().equals(UserRole.ADMIN)){
            return "forbidden";
        }

        Set<User> users = Arrays.stream(teamMembers.split(",")).map(id -> userService.getUserById(Long.parseLong(id))).collect(Collectors.toSet());

        Team team = new Team();
        team.setName(teamName);
        team.setUsers(users);

        teamService.saveTeam(team);

        return "redirect:/teams/view";
    }

    @GetMapping("/teams/view")
    public ModelAndView showTeams(HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");
        ModelAndView mv = new ModelAndView();
        List<Team> teams = null;


        if(user.getRole()== UserRole.ADMIN){
            teams = teamService.getAllTeams();
        }
        else{
            teams = userService.getUserById(user.getId()).getTeams().stream().toList();
        }
        mv.addObject("teams", teams);

        mv.setViewName("view_teams");

        mv.addObject("user", (User)request.getSession().getAttribute("user"));

        return mv;

    }

    @GetMapping("/teams/view/{id}")
    public ModelAndView viewSpecificTeam(HttpServletRequest request, @PathVariable(name = "id")Long teamId){

        Team team = teamService.getTeamById(teamId);
        User user = (User)request.getSession().getAttribute("user");

        if(user.getRole() == UserRole.USER){
            if(!userService.getUserById(user.getId()).getTeams().stream().filter(t -> t.getId().equals(team.getId())).findAny().isPresent())
                return  new ModelAndView("forbidden");
        }

        ModelAndView mv = new ModelAndView("view_team");

        mv.addObject("team", team);

        return mv;
    }

    @GetMapping("/teams/view/{id}/boards")
    public ModelAndView viewSpecificTeamBoards(HttpServletRequest request, @PathVariable(name = "id")Long teamId){

        Team team = teamService.getTeamById(teamId);
        User user = (User)request.getSession().getAttribute("user");

        if(user.getRole() == UserRole.USER){
            if(!userService.getUserById(user.getId()).getTeams().stream().filter(t -> t.getId().equals(team.getId())).findAny().isPresent())
                return  new ModelAndView("forbidden");
        }

        ModelAndView mv = new ModelAndView("view_team_boards");

        mv.addObject("team", team);

        return mv;
    }

    @GetMapping("/teams/edit/{id}")
    public ModelAndView editTeamsView(
            HttpServletRequest request,
            @PathVariable(name = "id") Long teamId
    ) {


        User user = (User)request.getSession().getAttribute("user");

        if(user.getRole() == UserRole.USER){
            return  new ModelAndView("forbidden");
        }

        ModelAndView mv = new ModelAndView("edit_team");

        Team team = teamService.getTeamById(teamId);
        List<User> allUsers = userService.getAllUsers();
        Set<Long> currUserIDsInTeam = team.getUsers().stream().map(u -> u.getId()).collect(Collectors.toSet());

        mv.addObject("team", team);
        mv.addObject("allUsers", allUsers);
        mv.addObject("currUserIDsInTeam", currUserIDsInTeam);

        return mv;

    }
    @PostMapping("/teams/edit/{id}")
    public String editTeams(
            HttpServletRequest request,
            @PathVariable(name = "id") Long teamId,
            @RequestParam(name = "team-name") String teamName,
            @RequestParam(name = "team-members") String teamMembers
    ){

        Team team = teamService.getTeamById(teamId);

        Set<User> users = Arrays.stream(teamMembers.split(",")).map(id -> userService.getUserById(Long.parseLong(id))).collect(Collectors.toSet());

        team.setName(teamName);
        team.setUsers(users);

        teamService.updateTeam(team);

        return "redirect:/teams/view";
    }
}
