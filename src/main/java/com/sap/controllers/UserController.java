package com.sap.controllers;

import com.sap.Service.RoleService;
import com.sap.Service.UserService;
import com.sap.models.Role;
import com.sap.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Resource
    private User user;
    @Resource
    private  UserService userService;
    @Resource
    private RoleService roleService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    private String Register(Model model, WebRequest request){

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

        List<Role> roles = new ArrayList<Role>();
        Role role = this.roleService.getRoleById(1);
        roles.add(role);
        this.user.setRoles(roles);

        try {
            userService.addUser(this.user);
            model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "login";
    }

//    @ResponseBody
//    @RequestMapping(value = "/create/role", method = RequestMethod.POST)
//    public void createItem(@RequestBody Role role){
//        roleService.addRole(role);
//    }


    @RequestMapping(value = "/create/role")
    public String createRoles(){
        Role role = new Role();
        role.setRole("ROLE_OWNER");
        this.roleService.addRole(role);
        return "homepage";
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
