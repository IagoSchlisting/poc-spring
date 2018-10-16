package com.sap.service.impl;
import com.sap.dao.UserDao;
import com.sap.dto.PassDTO;
import com.sap.dto.UserDTO;
import com.sap.models.UserDay;
import com.sap.service.TeamService;
import com.sap.service.UserDayService;
import com.sap.service.UserService;
import com.sap.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

public class UserServiceImp implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private TeamService teamService;
    @Resource
    private UserDayService userDayService;

    @Override
    public User addUser(UserDTO user) {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty()){
            throw new IllegalArgumentException("Username or password cannot be empty!");
        }
        if(userAlreadyExists(user.getUsername())){
            throw new IllegalArgumentException("Not possible to register the user in the system! Username already exists.");
        }
        if(!new String(user.getPassword()).equals(user.getConfirmPassword())){
            throw new IllegalArgumentException("Passwords doesn't match!");
        }

        user.setEnabled(true);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        User new_user = convertDTO(user);
        this.userDao.addUser(new_user);
        return new_user;
    }

    @Override
    public User getUserByName(String name){ return this.userDao.getUserByName(name);}

    @Override
    public void updateUser(UserDTO user) {
        if(user.getUsername().isEmpty()){
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if (!new String(user.getUsername()).equals(this.getUserById(user.getId()).getUsername())){
            if (userAlreadyExists(user.getUsername())) {
                throw new IllegalArgumentException("Not possible to edit member! Username already exists.");
            }
        }
        User original = this.getUserById(user.getId());
        original.setUsername(user.getUsername());
        original.setTeam(this.teamService.getTeamById(user.getTeamId()));
        this.userDao.updateUser(original);
    }

    @Override
    public void updateUserPass(PassDTO pass){
        if(pass.getOldPassword().isEmpty() || pass.getNewPassword().isEmpty() || pass.getConfirmPassword().isEmpty()){
            throw new IllegalArgumentException("Fields can't be empty!");
        }
        if(!passwordEncoder().matches(pass.getOldPassword(), pass.getUser().getPassword())){
            throw new IllegalArgumentException("Current pass doesn't match!");
        }
        if(!new String(pass.getNewPassword()).equals(pass.getConfirmPassword())){
            throw new IllegalArgumentException("Passwords doesn't match!");
        }
        pass.getUser().setPassword(passwordEncoder().encode(pass.getNewPassword()));
        this.userDao.updateUser(pass.getUser());
    }

    @Override
    public List<User> listUsers(int team_id, int user_id) {
        return this.userDao.listUsers(team_id, user_id);
    }

    @Override
    public List<User> listUsers(int team_id) {
        return this.userDao.listUsers(team_id);
    }

    @Override
    public User getUserById(int id) {
        return this.userDao.getUserById(id);
    }

    @Override
    public void removeUser(int id) {

        //Remove UserDays from user before
        List<UserDay> userDays = this.userDayService.listUserDaysByUser(id);
        for(UserDay userDay : userDays){
            this.userDayService.removeUserDay(userDay);
        }
        this.userDao.removeUser(id);
    }


    /**
     * Convert DTO to original user
     * @param user
     * @return original user
     */
    private User convertDTO(UserDTO user){
        User new_user = new User();
        new_user.setUsername(user.getUsername());
        new_user.setPassword(user.getPassword());
        new_user.setTeam(user.getTeam());
        new_user.setRoles(user.getRoles());
        new_user.setEnabled(user.getEnabled());
        return new_user;
    }

    /**
     * Verify if username already exists
     * @param username
     * @return boolean
     */
    public Boolean userAlreadyExists(String username){
        return this.getUserByName(username).getUsername() != null;
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

    /**
     * @return encoder
     */
    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
