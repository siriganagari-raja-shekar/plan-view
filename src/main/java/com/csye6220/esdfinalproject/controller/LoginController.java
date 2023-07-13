package com.csye6220.esdfinalproject.controller;

import com.csye6220.esdfinalproject.model.User;
import com.csye6220.esdfinalproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {


    @Autowired
    UserService userService;
    @GetMapping("/login*")
    public ModelAndView redirectToLoginPage(){
        return new ModelAndView("login");
    }

    @PostMapping("/perform-login")
    public ModelAndView checkValidation(@RequestParam String username, @RequestParam String password, HttpServletRequest request){

        ModelAndView modelAndView = new ModelAndView();

        User user = userService.getUserByEmail(username);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if(user == null){
            modelAndView.setViewName("redirect:/login");
            modelAndView.addObject("error", "email-wrong");
        }
        else{
            if(bCryptPasswordEncoder.matches(password, user.getPassword())){
                modelAndView.setViewName("redirect:/");
                request.getSession().setAttribute("user", user);
            }
            else {
                modelAndView.setViewName("redirect:/login");
                modelAndView.addObject("error", "password-wrong");
            }
        }
        return modelAndView;
    }
}
