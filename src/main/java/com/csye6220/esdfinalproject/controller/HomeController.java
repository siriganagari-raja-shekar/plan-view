package com.csye6220.esdfinalproject.controller;


import com.csye6220.esdfinalproject.model.User;
import com.csye6220.esdfinalproject.model.UserRole;
import com.csye6220.esdfinalproject.service.BoardService;
import com.csye6220.esdfinalproject.service.CardService;
import com.csye6220.esdfinalproject.service.TeamService;
import com.csye6220.esdfinalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {


    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CardService cardService;


    @GetMapping("/")
    public ModelAndView helloWorld(){
        ModelAndView mv = new ModelAndView("index");

//        userService.addUser(new User("Micheal", "Myers", "Software Engineer 1", UserRole.USER, "micheal123@gmail.com", new BCryptPasswordEncoder().encode("micheal123") ));
//        userService.addUser(new User("Paul", "Adams", "Admin One", UserRole.ADMIN, "adminone@gmail.com", new BCryptPasswordEncoder().encode("adminone")));
//        userService.addUser(new User("Neil", "Smith", "Admin Two", UserRole.ADMIN, "admintwo@gmail.com", new BCryptPasswordEncoder().encode("admintwo")));

        mv.addObject("usersCount", userService.getAllUsers().size());
        mv.addObject("teamsCount", teamService.getAllTeams().size());
        mv.addObject("boardsCount", boardService.getAllBoards().size());
        mv.addObject("cardsCount", cardService.getAllCardsCount());

        return mv;
    }
}
