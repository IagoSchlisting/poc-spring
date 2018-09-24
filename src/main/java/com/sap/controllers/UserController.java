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
import java.util.List;

@Controller
public class UserController {

    @Resource
    private User user;
    @Resource
    private Team team;
    @Resource
    private  UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private TeamService teamService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    private String registerOwner(Model model, WebRequest request){

        if(request.getParameter("new_username").isEmpty() ||
                request.getParameter("new_password").isEmpty()){
            model.addAttribute("error", "Username or pass can't be empty!");
            return "login";
        }

        this.user = new User();
        String username = request.getParameter("new_username");
        String pass = request.getParameter("new_password");
        String confirm_pass = request.getParameter("confirm_password");

        if(!new String(pass).equals(confirm_pass)){
            model.addAttribute("error", "Passwords doesn't match!");
            return "login";
        }

        String encryptedPassword = passwordEncoder().encode(pass);
        this.user.setUsername(username);
        this.user.setPassword(encryptedPassword);
        this.user.setEnabled(true);

        giveRoles(false);
        createOwnersTeam(username);
        this.user.setTeam(this.team);

        try {
            userService.addUser(this.user);
            model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "login";
    }

    public void createOwnersTeam(String username){
        this.team = new Team();
        this.team.setName(username + "'s team");
        this.teamService.addTeam(this.team);
    }


    @RequestMapping("/user/delete/{id}")
    public String removeUser(@PathVariable("id") int id, Model model, WebRequest request){
        userService.removeUser(id);
        User user = userService.getUserByName(request.getUserPrincipal().getName());
        model.addAttribute("team", user.getTeam());
        model.addAttribute("members", userService.listUsers(user.getTeam().getId(), user.getId()));
        return "ownerpage";
    }

    @RequestMapping("/user/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "add-edit-user";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String addUser(){
        return "add-edit-user";
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String saveUser(Model model, WebRequest request){

        if(request.getParameter("username").isEmpty()){
            model.addAttribute("error", "Username can't be empty!");
            return "add-edit-user";
        }

        Team team = userService.getUserByName(request.getUserPrincipal().getName()).getTeam();

        this.user = new User();
        String username = request.getParameter("username");
        String encryptedPassword = passwordEncoder().encode("password");
        this.user.setUsername(username);
        this.user.setPassword(encryptedPassword);
        this.user.setEnabled(true);

        this.user.setTeam(team);

        giveRoles(true);

        try {
            userService.addUser(this.user);
            model.addAttribute("msg", "New member registered successfully!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "add-edit-user";
    }


    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String saveEditedUser(Model model, WebRequest request){
        this.user = this.userService.getUserById(Integer.parseInt(request.getParameter("id")));
        this.user.setUsername(request.getParameter("username"));
        try {
            userService.updateUser(this.user);
            model.addAttribute("msg", "Member edited successfully!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "add-edit-user";
    }

    public void giveRoles(Boolean member){
        List<Role> roles = new ArrayList<Role>();

        if(roleService.getRoleById(1) == null){
            Role new_role_user = new Role();
            new_role_user.setRole("ROLE_USER");
            roleService.addRole(new_role_user);
        }
        Role role_user = roleService.getRoleById(1);


        if(roleService.getRoleById(2) == null){
            Role new_role_owner = new Role();
            new_role_owner.setRole("ROLE_OWNER");
            roleService.addRole(new_role_owner);
        }
        Role role_owner = roleService.getRoleById(2);

        if(roleService.getRoleById(3) == null){
            Role new_role_member = new Role();
            new_role_member.setRole("ROLE_MEMBER");
            roleService.addRole(new_role_member);
        }
        Role role_member = roleService.getRoleById(3);

        roles.add(role_user);
        if(!member){ roles.add(role_owner); }
        else { roles.add(role_member); }
        this.user.setRoles(roles);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
