package com.sap.controllers;

import com.sap.models.*;
import com.sap.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class PageController extends CommonController {

    @Resource
    private PeriodService periodService;
    @Resource
    private TeamService teamService;
    @Resource
    private DayService dayService;
    @Resource
    private NotificationService notificationService;
    @Resource
    private UserNotificationService userNotificationService;



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
     * Verify if user is from owner or member type and redirects accordingly
     * @param model
     * @param request
     * @return principal owner's or member's page
     */
    @RequestMapping("/")
    public String initialPage(Model model, WebRequest request){

        User principal = this.getPrincipalUser();
        model.addAttribute("principal", principal);
        List<Notification> notifications = this.notificationService.listNotifications(principal.getTeam().getId());
        model.addAttribute("notifications", notifications);

        for (Role role: principal.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){
                model.addAttribute("members", this.userService.listUsers(principal.getTeam().getId(), principal.getId()));
                return "ownerpage";
            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){
                model.addAttribute("member", true);
                model.addAttribute("periods", periodService.listPeriods(principal.getTeam().getId()));
                return "memberpage";
            }
        }

        return "login";
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
     * Method responsible for editing user
     * @param id
     * @param model
     * @return edit page
     */
    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
    public String editMember(@PathVariable("id") int id, Model model){
        if(this.userService.notAuthorized(id)){return "errors/403";}
        List<Team> teams = this.teamService.listTeams();
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("teams", teams);
        return "add-edit-user";
    }

    /**
     * Only redirects to the changepass user's page
     * @return changepass page
     */
    @RequestMapping(value = "/changepass", method = RequestMethod.GET)
    public String changePassPage(){
        return "changepass";
    }


    /**
     * Renderize the calendar-admin page
     * @param model
     * @return calendar page
     */
    @RequestMapping(value = "/calendar/admin", method = RequestMethod.GET)
    public String CalendarPage(Model model){
        User principal = this.getPrincipalUser();
        model.addAttribute("periods", periodService.listPeriods(principal.getTeam().getId()));
        return "calendar-admin";
    }

    /**
     * Renderize the period page
     * @param id
     * @param model
     * @return period page
     */
    @RequestMapping(value = "/calendar/manage/{id}", method = RequestMethod.GET)
    public String PeriodPage(@PathVariable("id") int id, Model model){
        if(this.periodService.notAuthorized(id)){return "errors/403";}
        if(this.getPrincipalUser().getRoles().get(1).getRole().equals("ROLE_MEMBER")){
            model.addAttribute("member", true);
        }
        model.addAttribute("days", this.dayService.listDays(id));
        return "period-days";
    }

    /**
     * Renderize day page
     * @param id
     * @param model
     * @return day page
     */
    @RequestMapping(value = "/day/{id}", method = RequestMethod.GET)
    public String DayPage(@PathVariable("id") int id, Model model){
        User principal = this.getPrincipalUser();
        model.addAttribute("day", dayService.getDayById(id));

        if(this.dayService.notAuthorized(id)){return "errors/403";}

        for(Role role: principal.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){
                model.addAttribute("userDays", userDayService.listUserDays(id));
                return "owner-day-manager";
            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){
                model.addAttribute("member", true);
                model.addAttribute("userDay", userDayService.findUserDay(principal.getId(), id));
                return "member-day-manager";
            }
        }
        return "login";
    }

    @RequestMapping(value = "/calendar/configure/{id}", method = RequestMethod.GET)
    public String CalendarConfigurePage(@PathVariable("id") int id, Model model){
        if(this.periodService.notAuthorized(id)){return "errors/403";}
        model.addAttribute("days", this.dayService.listDays(id));
        return "calendar-configure";
    }

    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public  String NotificationsPage(Model model){
        int team_id  = this.getPrincipalUser().getTeam().getId();
        model.addAttribute("notifications", this.notificationService.listNotifications(team_id));
        return "notification";
    }


    /**
     * Redirects to 403 page
     * @return not authorized page
     */
    @RequestMapping("/403")
    public String notAllowed(){
        return "errors/403";
    }
}
