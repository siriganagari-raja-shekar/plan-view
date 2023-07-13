package com.csye6220.esdfinalproject.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout*")
    public String handleLogout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

}
