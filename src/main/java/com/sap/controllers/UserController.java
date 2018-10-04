package com.sap.controllers;

import com.sap.service.RoleService;
import com.sap.service.TeamService;
import com.sap.models.Role;
import com.sap.models.Team;
import com.sap.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController extends BaseController{

    @Resource
    private RoleService roleService;
    @Resource
    private TeamService teamService;

    /**
     * Method responsible for registering a new OWNER in the system. New member type users don't pass through this method.
     * @param model
     * @param request
     * @return / redirects to the login page with a success or error message
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    private ModelAndView addOwner(Model model, WebRequest request){
        model.addAttribute("stay", true);
        String username = request.getParameter("new_username");
        String pass = request.getParameter("new_password");
        String confirm_pass = request.getParameter("confirm_password");

        String error = "";
        if(username.isEmpty() || pass.isEmpty()){ error = "Username or password cannot be empty!"; }
        if(userAlreadyExists(username)){ error = "Not possible to register the user in the system! Username already exists."; }
        if(!new String(pass).equals(confirm_pass)){ error = "Passwords doesn't match!"; }

        if(!error.isEmpty()){
            model.addAttribute("error", error);
            return new ModelAndView("/login");
        }
        try
        {
            User user = new User();
            user.setEnabled(true);
            user.setUsername(username);
            user.setPassword(passwordEncoder().encode(pass));
            user.setRoles(giveRoles(true));
            user.setTeam(createOwnersTeam(username));
            userService.addUser(user);
            model.addAttribute("stay", false);
            model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return new ModelAndView("/login");
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
     * Method responsible for deleting member
     * @param id
     * @param model
     * @return owner's page
     */
    @RequestMapping("/user/delete/{id}")
    public RedirectView removeMember(@PathVariable("id") int id, Model model){
        User principal = this.getPrincipalUser();
        if(notAuthorized(principal, id)){ return new RedirectView("/403");  }
        userService.removeUser(id);
        return new RedirectView("/");
    }

    /**
     * Verify if user is not trying to execute an action at himself or at some member that is not from his team.
     * @param principal
     * @param id
     * @return Boolean
     */
    public Boolean notAuthorized(User principal, int id) {
        // User cannot execute action at himself !IMPORTANT
        if (principal.getId() == id) {
            return true;
        }
        User user = userService.getUserById(id);
        // Owner cannot execute an action with a member that is not from his team !IMPORTANT
        if (principal.getTeam().getId() != user.getTeam().getId()) {
            return true;
        }
        return false;
    }

    /**
     * Method responsible for editing user
     * @param id
     * @param model
     * @return edit page
     */
    @RequestMapping("/user/edit/{id}")
    public String editMember(@PathVariable("id") int id, Model model){
        User principal = this.getPrincipalUser();
        if(notAuthorized(principal, id)){return "errors/403";}
        List<Team> teams = teamService.listTeams();
        model.addAttribute("user", userService.getUserById(id));
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

        if(username.isEmpty()){
            model.addAttribute("error", "Username can't be empty!");
            return "add-edit-user";
        }
        if(userAlreadyExists(username)){
            model.addAttribute("error", "Not possible to add the member! Username already exists.");
            return "add-edit-user";
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder().encode("password"));
            user.setEnabled(true);
            user.setRoles(giveRoles(false));
            user.setTeam(this.getPrincipalUser().getTeam());
            userService.addUser(user);
            model.addAttribute("msg", "New member registered successfully!");
        }
        catch (Exception e){
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
    public String saveEditedMember(Model model, WebRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        int team_id = Integer.parseInt(request.getParameter("team"));
        String username = request.getParameter("username");

        User user = this.userService.getUserById(id);
        Team team = this.teamService.getTeamById(team_id);

        model.addAttribute("user", user);
        model.addAttribute("teams", teamService.listTeams());

        User principal = this.getPrincipalUser();

        if(notAuthorized(principal, id)){return "errors/403";}

        if (username.isEmpty()) {
            model.addAttribute("error", "Username can't be empty!");
            return "add-edit-user";
        }
        if (!new String(user.getUsername()).equals(username)){
            if (userAlreadyExists(username)) {
                model.addAttribute("error", "Not possible to edit member! Username already exists.");
                return "add-edit-user";
            }
        }
        try
        {
            user.setUsername(username);
            user.setTeam(team);
            userService.updateUser(user);
            model.addAttribute("msg", "Member edited successfully!");
        }
        catch (Exception e){ model.addAttribute("error", e.getMessage()); }
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

        if(oldpass.isEmpty() || newpass.isEmpty() || confirmpass.isEmpty()){
            model.addAttribute("error", "Fields can't be empty!");
            return "changepass";
        }

        User principal = this.getPrincipalUser();
        String userpass = principal.getPassword();

        if(!passwordEncoder().matches(oldpass, userpass)){
            model.addAttribute("error", "Current pass doesn't match!");
            return "changepass";
        }

        if(!new String(newpass).equals(confirmpass)){
            model.addAttribute("error", "Passwords doesn't match!");
            return "changepass";
        }

        try{
            principal.setPassword(passwordEncoder().encode(newpass));
            userService.updateUser(principal);
            model.addAttribute("msg", "Password changed successfully!");
        }
        catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "changepass";
    }
}
