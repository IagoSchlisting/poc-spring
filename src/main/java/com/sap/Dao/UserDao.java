package com.sap.Dao;

import com.sap.models.User;

import java.util.List;

public interface UserDao {
    public void addUser(User p);
    public User getUserByName(String name);
    public void updateUser(User p);
    public List<User> listUsers();
    public User getUserById(int id);
    public void removeUser(int id);
}
