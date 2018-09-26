package com.sap.service.impl;
import com.sap.dao.UserDao;
import com.sap.service.UserService;
import com.sap.models.User;

import javax.annotation.Resource;
import java.util.List;

public class UserServiceImp implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public void addUser(User user) {
        this.userDao.addUser(user);
    }

    @Override
    public User getUserByName(String name){ return this.userDao.getUserByName(name);}

    @Override
    public void updateUser(User user) {
        this.userDao.updateUser(user);
    }

    @Override
    public List<User> listUsers(int team_id, int user_id) {
        return this.userDao.listUsers(team_id, user_id);
    }

    @Override
    public User getUserById(int id) {
        return this.userDao.getUserById(id);
    }

    @Override
    public void removeUser(int id) {
        this.userDao.removeUser(id);
    }
}
