package com.sap.service.impl;
import com.sap.dao.UserDao;
import com.sap.service.UserService;
import com.sap.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

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
        if(notAuthorized(user.getId())){ throw new RuntimeException("You are not allowed to execute this action, you idiot."); }
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
        if(notAuthorized(id)){ throw new RuntimeException(); }
        this.userDao.removeUser(id);
    }

    /**
     * Verify if user is not trying to execute an action at himself or at some member that is not from his team.
     * @param id
     * @return Boolean
     */
    public Boolean notAuthorized(int id) {
        User principal = this.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        // User cannot execute action at himself !IMPORTANT
        if (principal.getId() == id) {
            return true;
        }
        User user = this.getUserById(id);
        // Owner cannot execute an action with a member that is not from his team !IMPORTANT
        if (principal.getTeam().getId() != user.getTeam().getId()) {
            return true;
        }
        return false;
    }

}
