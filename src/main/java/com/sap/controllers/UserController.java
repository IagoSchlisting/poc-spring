package com.sap.controllers;

import com.sap.dto.PassDTO;
import com.sap.dto.UserDTO;
import com.sap.models.*;
import com.sap.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController extends CommonController {

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
     * @param redirectAttributes
     * @return / redirects to the login page with a success or error message
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    private RedirectView addOwner(RedirectAttributes redirectAttributes, UserDTO user){
        try{
            user.setRoles(giveRoles(true));
            Team team = createOwnersTeam(user.getUsername());
            user.setTeam(team);
            this.userService.addUser(user);
            redirectAttributes.addFlashAttribute("msg", "You've been registered successfully. Try to Log in!");
        }
        catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("stay", true);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/login");
    }

    /**
     * Method responsible for creating new team based on owner's username.
     * @param username
     * @return team
     */
    public Team createOwnersTeam(String username){
        Team team = new Team();

        if(username.isEmpty()){
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if(this.userService.userAlreadyExists(username)){
            throw new IllegalArgumentException("Username already exists!");
        }

        for(Team t: this.teamService.listTeams()){
            if(t.getName().equals(username + "'s team")){
                return t;
            }
        }

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
     * Responsible for adding new member. Action called through the owners interface
     * @param redirectAttributes
     * @return success or error message in the add/edit page
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public RedirectView addMember(RedirectAttributes redirectAttributes, UserDTO user){
        try{
            user.setRoles(giveRoles(false));
            user.setTeam(this.getPrincipalUser().getTeam());
            user.setPassword("password");
            user.setConfirmPassword("password");
            User new_user = this.userService.addUser(user);
            this.boundNewUserToPeriods(new_user);
            redirectAttributes.addFlashAttribute("msg", "New member registered successfully!");
        }
        catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/user/add");
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
     * @param redirectAttributes
     * @return success or error message in the add/edit page
     */
    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public RedirectView saveEditedMember(RedirectAttributes redirectAttributes, UserDTO user) {
        if(this.userService.notAuthorized(user.getId())){ new RedirectView("/403");}
        try{
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("msg", "Member edited successfully!");
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/user/edit/" + user.getId());
    }

    /**
     * Allows user to change password when needed
     * @param redirectAttributes
     * @return success or error message in the changepass page
     */
    @RequestMapping(value = "/changepass", method = RequestMethod.POST)
    public RedirectView changePassdb(RedirectAttributes redirectAttributes, PassDTO pass){
        try{
            pass.setUser(this.getPrincipalUser());
            userService.updateUserPass(pass);
            redirectAttributes.addFlashAttribute("msg", "Password changed successfully!");
        }
        catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/changepass");
    }
}
