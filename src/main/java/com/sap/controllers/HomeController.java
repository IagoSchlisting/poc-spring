package com.sap.controllers;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sap.Service.UserService;

import javax.annotation.Resource;


@Controller
public class HomeController {

    @Resource
    private UserService userService;

    @RequestMapping("/")
    public String homepage(Model model){
        model.addAttribute("users", userService.listUsers());
        return "homepage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }
        return "login";
    }

}
