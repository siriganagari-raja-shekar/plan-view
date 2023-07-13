package com.csye6220.esdfinalproject.controller;


import com.csye6220.esdfinalproject.model.User;
import com.csye6220.esdfinalproject.model.UserRole;
import com.csye6220.esdfinalproject.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Validated
public class RegistrationController {

    @Autowired
    UserService userService;

    @GetMapping("/signup")
    public String redirectToRegistrationPage(){
        return "signup";
    }

    @PostMapping("/register")
    public ModelAndView registerUser(
            @RequestParam @Email(message = "Email is not valid") String email,
            @RequestParam @Size(min=8, max=15, message = "Password length must be between 8 and 15 characters") String password,
            @RequestParam(name = "firstname") @Size(min = 2, message = "First Name should be atleast 2 characters") String firstName,
            @RequestParam(name = "lastname") @Size(min = 2, message = "Last Name should be atleast 2 characters") String lastName,
           @RequestParam(name = "job-title") @Size(min = 3, message = "Job Title should be atleast 3 characters") String jobTitle
    ){

        ModelAndView modelAndView = new ModelAndView();

        User existingUser = userService.getUserByEmail(email);

        if(existingUser != null){
            modelAndView.setViewName("redirect:/signup");
            modelAndView.addObject("error", "email-exists");
            return modelAndView;
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User(firstName, lastName, jobTitle, UserRole.USER, email, bCryptPasswordEncoder.encode(password));

        userService.addUser(user);

        modelAndView.addObject("success", "registration-successful");
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView constraintViolationException(ConstraintViolationException ex){
        Set<String> errors = new HashSet<>();
        if(ex.getConstraintViolations() == null){
            errors.add(ex.getMessage());
        }
        else{
            errors = ex.getConstraintViolations().stream().map(v -> v.getMessage()).collect(Collectors.toSet());
        }
        return new ModelAndView("error", "errors", errors);
    }
}
