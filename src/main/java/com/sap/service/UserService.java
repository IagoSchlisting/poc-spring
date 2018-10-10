package com.sap.service;
import com.sap.dto.PassDTO;
import com.sap.dto.UserDTO;
import com.sap.models.User;

import java.util.List;

public interface UserService {

    public User addUser(UserDTO user);
    public void updateUser(UserDTO user);
    public void updateUserPass(PassDTO pass);
    public User getUserByName(String name);
    public List<User> listUsers(int team_id, int user_id);
    public User getUserById(int id);
    public void removeUser(int id);
    public Boolean notAuthorized(int id);
    public Boolean userAlreadyExists(String username);
}
