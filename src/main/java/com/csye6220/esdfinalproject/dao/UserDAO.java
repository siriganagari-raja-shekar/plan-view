package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.User;

import java.util.List;

public interface UserDAO {

    public void save(User user);

    public void update(User user);

    public void delete(User user);

    public void deleteById(Long id);

    public void deleteByEmail(String email);

    public User getById(Long id);

    public User getByEmail(String username);

    public List<User> getAllUsers();

}
