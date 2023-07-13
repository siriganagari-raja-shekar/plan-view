package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.model.User;
import com.csye6220.esdfinalproject.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserServiceImpl();
        User user = userService.getUserByEmail("siriganagari.r@northeastern.edu");
        userService.deleteUser(user);
    }

    @Test
    public void createUserTest(){

        userService.addUser(new User("Raja Shekar Reddy", "Siriganagari", "Software Engineer 1", UserRole.USER, "siriganagari.r@northeastern.edu", "pass"));

        User user = userService.getUserByEmail("siriganagari.r@northeastern.edu");

        assertEquals(user.getFirstName(), "Raja Shekar");

    }
}
