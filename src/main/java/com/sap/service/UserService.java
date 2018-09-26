package com.sap.service;
import com.sap.models.User;

import java.util.List;

public interface UserService {

    public void addUser(User user);
    public User getUserByName(String name);
    public void updateUser(User user);
    public List<User> listUsers(int team_id, int user_id);
    public User getUserById(int id);
    public void removeUser(int id);

}
