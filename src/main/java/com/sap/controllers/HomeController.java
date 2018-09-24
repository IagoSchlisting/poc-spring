package com.sap.controllers;
import com.sap.Service.RoleService;
import com.sap.Service.TeamService;
import com.sap.models.Role;
import com.sap.models.Team;
import com.sap.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sap.Service.UserService;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.security.Principal;


@Controller
public class HomeController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private Team team;

    @RequestMapping("/")
    public String initialPage(Model model, WebRequest request){
        User user = userService.getUserByName(request.getUserPrincipal().getName());
        model.addAttribute("principal", user);
        for (Role role: user.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){
                model.addAttribute("team", user.getTeam());
                model.addAttribute("members", this.userService.listUsers(user.getTeam().getId(), user.getId()));
                return "ownerpage";
            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){
                return "memberpage";
            }
        }
        return "login";
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
