package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.model.User;

import java.util.List;

public interface UserService {

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(User user);

    public User getUserById(long id);

    public User getUserByEmail(String email);

    public void deleteUserById(long id);

    public void deleteUserByEmail(String email);

    public List<User> getAllUsers();

}
