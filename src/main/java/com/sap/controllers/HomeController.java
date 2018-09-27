package com.sap.controllers;

import com.sap.service.UserService;
import com.sap.models.Role;
import com.sap.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

@Controller
public class HomeController {

    @Resource
    private UserService userService;


    /**
     * Verify if user is from owner or member type and redirects accordingly
     * @param model
     * @param request
     * @return principal owner's or member's page
     */
    @RequestMapping("/")
    public String initialPage(Model model, WebRequest request){
        // Get current user
        User user = userService.getUserByName(request.getUserPrincipal().getName());
        // Return user and team information to the view
        model.addAttribute("principal", user);
        model.addAttribute("team", user.getTeam());

        // Iterate through all roles from user and redirects accordingly
        for (Role role: user.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){
                // If user is from the owner type,  return all corresponding  members to the view
                model.addAttribute("members", this.userService.listUsers(user.getTeam().getId(), user.getId()));
                return "ownerpage";
            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){
                // Waiting for more code here
                return "memberpage";
            }
        }
        return "login";
    }

    /**
     * Login validation method
     * @param error
     * @param logout
     * @param model
     * @return to corresponding page, login or home
     */
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

    /**
     * 403 Error page
     * @return redirects
     */
    @RequestMapping("/403")
    public String notFound(){
        return "errors/403";
    }

}
