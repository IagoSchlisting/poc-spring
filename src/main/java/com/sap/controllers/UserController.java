package com.sap.controllers;

import com.sap.service.RoleService;
import com.sap.service.TeamService;
import com.sap.service.UserService;
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
    private UserService userService;
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

        // Getting data from form
        String username = request.getParameter("new_username");
        String pass = request.getParameter("new_password");
        String confirm_pass = request.getParameter("confirm_password");

        // Checking username and password so they are not empty
        if(username.isEmpty() || pass.isEmpty()){
            model.addAttribute("error", "Username or password cannot be empty!");
            model.addAttribute("stay", true);
            return "login";
        }

         //Validate username
        if(userAlreadyExists(username)){
            model.addAttribute("error", "Not possible to register the user in the system! Username already exists.");
            model.addAttribute("stay", true);
            return "login";
        }
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

        try
        {
            List<Role> roles = giveRoles(true); // If owner == true , then user receives owner access
            Team team = createOwnersTeam(username); // Creates team based on owners username
            user.setRoles(roles);
            user.setTeam(team);
            userService.addUser(user);
            model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        }
        catch (Exception e){
            // If not able to register, it will be spit a gross message in the screen.
            model.addAttribute("stay", true);
            model.addAttribute("error", e.getMessage());
        }
        return "login";
    }

    /**
     * Verify if username already exists
     * @param username
     * @return boolean
     */
    public Boolean userAlreadyExists(String username){
        return userService.getUserByName(username).getUsername() != null;
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

        for (String role : roles_string) {
            if (roleService.getRoleById(roles_string.indexOf(role) + 1) == null) {
                new_role = new Role();
                new_role.setRole(role);
                roleService.addRole(new_role);
            }
        }
        roles.add(roleService.getRoleById(1));
        if(owner){roles.add(roleService.getRoleById(2));}
        else{ roles.add(roleService.getRoleById(3));}
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
    public String editMember(@PathVariable("id") int id, Model model, WebRequest request){

        User principal = userService.getUserByName(request.getUserPrincipal().getName());
        // Owner cannot edit himself !IMPORTANT
            if(principal.getId() == id){
                return "errors/403";
            }
        // ------------------------
        User user = userService.getUserById(id);
        // Owner cannot edit member that is not from his team !IMPORTANT
            if(principal.getTeam().getId() != user.getTeam().getId()){
                return "errors/403";
            }
        // ------------------------

        List<Team> teams = teamService.listTeams();
        model.addAttribute("user", user);
        model.addAttribute("teams", teams);
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

        String username = request.getParameter("username");
        // Checking if username is not empty
        if(username.isEmpty()){
            model.addAttribute("error", "Username can't be empty!");
            return "add-edit-user";
        }
        //Validate username
        if(userAlreadyExists(username)){
            model.addAttribute("error", "Not possible to add the member! Username already exists.");
            return "add-edit-user";
        }

        String encryptedPassword = passwordEncoder().encode("password");
        // Get team from the principal user, who is registering the new member
        Team team = userService.getUserByName(request.getUserPrincipal().getName()).getTeam();
        // Get roles allowed to the member user type
        List<Role> roles = giveRoles(false); // Gives only member access to the user

        // Creating new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.setRoles(roles);
        user.setTeam(team);

        try
        {
            userService.addUser(user);
            model.addAttribute("msg", "New member registered successfully!");
        }
        catch (Exception e){
            // If not able to add new member, it will be spit a gross message in the screen.
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
        Team team = this.teamService.getTeamById(Integer.parseInt(request.getParameter("team")));
        String username = request.getParameter("username");

        List<Team> teams = teamService.listTeams();
        model.addAttribute("user", user);
        model.addAttribute("teams", teams);


        if(username.isEmpty()){
            model.addAttribute("error", "Username can't be empty!");
            return "add-edit-user";
        }
        //Validate username
        if(userAlreadyExists(username)){
            model.addAttribute("error", "Not possible to edit member! Username already exists.");
            return "add-edit-user";
        }
        user.setUsername(username);
        user.setTeam(team);
        try
        {
            userService.updateUser(user);
            model.addAttribute("msg", "Member edited successfully!");
        }
        catch (Exception e){
            // If not able to edit member, it will be spit a gross message in the screen.
            model.addAttribute("error", e.getMessage());
        }
        return "add-edit-user";
    }

    /**
     * Only redirects to the changepass user's page
     * @return changepass page
     */
    @RequestMapping(value = "/changepass", method = RequestMethod.GET)
    public String changePass(){
        return "changepass";
    }

    /**
     * Allows user to change password when needed
     * @param model
     * @param request
     * @return success or error message in the changepass page
     */
    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public String changePassdb(Model model, WebRequest request){

        String oldpass = request.getParameter("old_password");
        String newpass = request.getParameter("new_password");
        String confirmpass = request.getParameter("confirm_password");

        // Checking fields so they are not empty
        if(oldpass.isEmpty() || newpass.isEmpty() || confirmpass.isEmpty()){
            model.addAttribute("error", "Fields can't be empty!");
            return "changepass";
        }

        // Getting current user from Session
        User user = userService.getUserByName(request.getUserPrincipal().getName());
        String userpass = user.getPassword();

        if(!passwordEncoder().matches(oldpass, userpass)){
            model.addAttribute("error", "Current pass doesn't match!");
            return "changepass";
        }

        if(!new String(newpass).equals(confirmpass)){
            model.addAttribute("error", "Passwords doesn't match!");
            return "changepass";
        }

        String encryptedPassword = passwordEncoder().encode(newpass);

        try{
            user.setPassword(encryptedPassword);
            userService.updateUser(user);
            model.addAttribute("msg", "Password changed successfully!");
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "changepass";
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
