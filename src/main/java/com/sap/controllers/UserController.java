package com.sap.controllers;

import com.sap.Service.UserService;
import com.sap.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

@Controller
public class UserController {

    @Resource
    private UserService userService;
    private User user;

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

        this.user.setUsername(username);
        this.user.setPassword(pass);
        this.user.setOwner(true);
        userService.addUser(this.user);
        model.addAttribute("msg", "You've been registered successfully. Try to Log in!");
        return "login";
    }
}
