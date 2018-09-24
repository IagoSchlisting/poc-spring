package com.sap.Service.impl;
import com.sap.Dao.UserDao;
import com.sap.Service.UserService;
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
    public List<User> listUsers() {
        return this.userDao.listUsers();
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
