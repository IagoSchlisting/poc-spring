package com.sap.controllers;

import com.sap.Service.RoleService;
import com.sap.Service.TeamService;
import com.sap.Service.UserService;
import com.sap.models.Role;
import com.sap.models.Team;
import com.sap.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    @Resource
    private  UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private TeamService teamService;

    /**
     * Method responsible for registering a  new OWNER in the system. New member type users don't pass through this method.
     * @param model
     * @param request
     * @return / redirects to the login page with a success or error message
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    private String addOwner(Model model, WebRequest request){

        // Checking username and password so they are not empty
        if(request.getParameter("new_username").isEmpty() ||
                request.getParameter("new_password").isEmpty()){
            model.addAttribute("error", "Username or password cannot be empty!");
            model.addAttribute("stay", true);
            return "login";
        }

        // Getting data from form
        String username = request.getParameter("new_username");
        String pass = request.getParameter("new_password");
        String confirm_pass = request.getParameter("confirm_password");

        // Checking if passwords are equal and encrypting it
        if(!new String(pass).equals(confirm_pass)){
            model.addAttribute("error", "Passwords doesn't match!");
            model.addAttribute("stay", true);
            return "login";
        }
        String encryptedPassword = passwordEncoder().encode(pass);

        //Creating new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEnabled(true);

        try {
            List<Role> roles = giveRoles(true); // If owner == true , then user receives owner access
            Team team = createOwnersTeam(username); // Creates team based on owners username
            user.setRoles(roles);
            user.setTeam(team);
            userService.addUser(user);
            model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        }catch (Exception e){
            // If not able to register, it will be spit a gross message in the screen.
            // ATTENTION: The most frequent error, it's trying to register an username witch already exists
            model.addAttribute("stay", true);
            model.addAttribute("error", e.getMessage());
        }

        return "login";
    }

    /**
     * Method responsible for creating new team based on owner's username.
     * @param username
     * @return team
     */
    public Team createOwnersTeam(String username){
        Team team = new Team();
        team.setName(username + "'s team");
        teamService.addTeam(team);
        return team;
    }

    /**
     * Method responsible for giving roles to new the new user based on their access variable.
     * @param owner
     * @return list of roles
     */
    public List<Role> giveRoles(Boolean owner){
        List<Role> roles = new ArrayList<Role>();
        List<String> roles_string = Arrays.asList("ROLE_USER", "ROLE_OWNER", "ROLE_MEMBER");
        Role new_role;

        for (int i = 0; i < roles_string.size(); i++) {
            if(roleService.getRoleById(i+1) == null){
                new_role = new Role();
                new_role.setRole(roles_string.get(i));
                roleService.addRole(new_role);
            }
            new_role = roleService.getRoleById(i+1);
            switch (i){
                case 0:
                    roles.add(new_role);
                    break;
                case 1:
                    if(owner){roles.add(new_role);}
                    break;
                case 2:
                    if(!owner){roles.add(new_role);}
                    break;
            }
        }
        return roles;
    }

    /**
     * Method responsible for deleting user
     * @param id
     * @param model
     * @param request
     * @return owner's page
     */
    @RequestMapping("/user/delete/{id}")
    public String removeMember(@PathVariable("id") int id, Model model, WebRequest request){
        userService.removeUser(id);
        User user = userService.getUserByName(request.getUserPrincipal().getName());
        model.addAttribute("team", user.getTeam());
        model.addAttribute("members", userService.listUsers(user.getTeam().getId(), user.getId()));
        return "ownerpage";
    }

    /**
     * Method responsible for editing user
     * @param id
     * @param model
     * @return edit page
     */
    @RequestMapping("/user/edit/{id}")
    public String editMember(@PathVariable("id") int id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "add-edit-user";
    }

    /**
     * Only redirects to the add user's page
     * @return add page
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String addMember(){
        return "add-edit-user";
    }

    /**
     * Responsible for adding new member. Action called through the owners interface
     * @param model
     * @param request
     * @return success or error message in the add/edit page
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String addMember(Model model, WebRequest request){

        // Checking username is not empty
        if(request.getParameter("username").isEmpty()){
            model.addAttribute("error", "Username can't be empty!");
            return "add-edit-user";
        }
        String username = request.getParameter("username");
        String encryptedPassword = passwordEncoder().encode("password");

        // Get team from the principal user, who is registering the new member
        Team team = userService.getUserByName(request.getUserPrincipal().getName()).getTeam();
        // Get roles allowed
        List<Role> roles = giveRoles(false); // Gives only member access to the user

        // Creating new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.setRoles(roles);
        user.setTeam(team);

        try {
            userService.addUser(user);
            model.addAttribute("msg", "New member registered successfully!");
        }catch (Exception e){
            // If not able to add new member, it will be spit a gross message in the screen.
            // ATTENTION: The most frequent error, it's trying to add an username witch already exists
            model.addAttribute("error", e.getMessage());
        }
        return "add-edit-user";
    }


    /**
     * Responsible for edit member type users
     * @param model
     * @param request
     * @return success or error message in the add/edit page
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String saveEditedMember(Model model, WebRequest request){
        User user = this.userService.getUserById(Integer.parseInt(request.getParameter("id")));
        user.setUsername(request.getParameter("username"));
        try {
            userService.updateUser(user);
            model.addAttribute("msg", "Member edited successfully!");
        }catch (Exception e){
            // If not able to edit member, it will be spit a gross message in the screen.
            // ATTENTION: The most frequent error, it's trying to edit to an username witch already exists
            model.addAttribute("error", e.getMessage());
        }
        return "add-edit-user";
    }

    /**
     * Encrypts the user's password before saving in the database
     * @return the new encrypted password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
