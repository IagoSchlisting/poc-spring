package com.sap.controllers;

import com.sap.dto.PassDTO;
import com.sap.dto.UserDTO;
import com.sap.models.*;
import com.sap.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController extends BaseController{

    @Resource
    private RoleService roleService;
    @Resource
    private TeamService teamService;
    @Resource
    private PeriodService periodService;
    @Resource
    private DayService dayService;

    /**
     * Method responsible for registering a new OWNER in the system. New member type users don't pass through this method.
     * @param model
     * @return / redirects to the login page with a success or error message
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    private ModelAndView addOwner(Model model, UserDTO user){
        try{
            user.setRoles(giveRoles(true));
            user.setTeam(createOwnersTeam(user.getUsername()));
            this.userService.addUser(user);
            model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        }
        catch (IllegalArgumentException e){
            model.addAttribute("stay", true);
            model.addAttribute("error", e.getMessage());
        }
        return new ModelAndView("/login");
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
        if(this.userService.notAuthorized(id)){return new RedirectView("/403");}
        userService.removeUser(id);
        return new RedirectView("/");
    }

    /**
     * Method responsible for editing user
     * @param id
     * @param model
     * @return edit page
     */
    @RequestMapping("/user/edit/{id}")
    public String editMember(@PathVariable("id") int id, Model model){
        if(this.userService.notAuthorized(id)){return "errors/403";}
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
     * @return success or error message in the add/edit page
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String addMember(Model model, UserDTO user){
        try{
            user.setRoles(giveRoles(false));
            user.setTeam(this.getPrincipalUser().getTeam());
            user.setPassword("password");
            user.setConfirmPassword("password");
            User new_user = this.userService.addUser(user);
            this.boundNewUserToPeriods(new_user);
            model.addAttribute("msg", "New member registered successfully!");
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        }

        return "add-edit-user";
    }

    /**
     * Responsible for bounding new created users to all periods created by the owner
     * @param user
     */
    private void boundNewUserToPeriods(User user){
        List<Period> periods = this.periodService.listPeriods(user.getTeam().getId());
        List<Day> days;

        for(Period period : periods){
            days = this.dayService.listDays(period.getId());
            for(Day day: days){
                if(!day.getDay().isBefore(LocalDate.now())){
                    this.createUserDay(user, day);
                }
            }
        }
    }

    /**
     * Responsible for edit member type users
     * @param model
     * @return success or error message in the add/edit page
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String saveEditedMember(Model model, UserDTO user) {
        if(this.userService.notAuthorized(user.getId())){return "errors/403";}
        try
        {
            user.setTeam(teamService.getTeamById(user.getTeamId()));
            userService.updateUser(user);
            model.addAttribute("user", user);
            model.addAttribute("teams", teamService.listTeams());
            model.addAttribute("msg", "Member edited successfully!");
        }
        catch (IllegalArgumentException e){ model.addAttribute("error", e.getMessage()); }
        return "add-edit-user";
    }

    /**
     * Allows user to change password when needed
     * @param model
     * @return success or error message in the changepass page
     */
    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public String changePassdb(Model model, PassDTO pass){
        try{
            pass.setUser(this.getPrincipalUser());
            userService.updateUserPass(pass);
            model.addAttribute("msg", "Password changed successfully!");
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        }
        return "changepass";
    }
}
