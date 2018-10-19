package com.sap.controllers;

import com.sap.dto.DayDTO;
import com.sap.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController extends CommonController {

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
     * Verify if user is an owner or an member and redirects accordingly
     * Furthermore, this method is responsible for treating the notification's logic,
     * in other words, it defines when a notification must or must not appear to the user
     * @param model
     * @return principal owner's or member's page
     */
    @RequestMapping("/")
    public String initialPage(Model model){

        User principal = this.getPrincipalUser();
        model.addAttribute("principal", principal);
        //List<Notification> notifications = this.notificationService.listNotifications(principal.getTeam().getId());

        List<Notification> notifications = this.notificationService.listNotificationsForUser(principal.getId());

        for (Role role: principal.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){

                model.addAttribute("notifications", notifications);
                model.addAttribute("members", this.userService.listUsers(principal.getTeam().getId(), principal.getId()));
                return "ownerpage";

            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){

                for(Notification notification: notifications){
                    for(UserDay userDay : this.userDayService.listUserDaysByUser(principal.getId())){

                        String day = userDay.getDay().getDay().toString();
                        String shift = userDay.getShift().toString();

                        if(notification.getMsg().contains(day) && notification.getMsg().contains(shift)){
                            UserNotification userNotification = this.userNotificationService.getUserNotification(notification.getId(), principal.getId());
                            userNotification.setVisualized(true);
                            this.userNotificationService.updateUserNotification(userNotification);

                            if(this.userDayService.getNeededShift(userDay.getDay(), true) == Shift.NONE){
                                this.notificationService.removeNotification(notification);
                            }
                        }

                    }
                }

                model.addAttribute("notifications", this.notificationService.listNotificationsForUser(principal.getId()));
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
     * Render the calendar-admin page
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
     * Render the period page
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
        List<DayDTO> daysDTO = new ArrayList<DayDTO>();
        DayDTO dayDTO;
        List<Day> days = this.dayService.listDays(id);

        for(Day day: days){
            dayDTO = new DayDTO();

            dayDTO.setId(day.getId());
            dayDTO.setDay(day.getDay());
            dayDTO.setNumberDay(day.getNumberDay());
            dayDTO.setNumberLate(day.getNumberLate());
            dayDTO.setSpecial(day.getSpecial());
            dayDTO.setMemberdays(day.getMemberdays());

            if(this.userDayService.getNeededShift(day, true) == Shift.NONE){
                dayDTO.setCompleted(true);
            }else{
                dayDTO.setCompleted(false);
            }

            daysDTO.add(dayDTO);
        }

        model.addAttribute("days", daysDTO);
        return "period-days";
    }

    /**
     * Render the day page
     * @param id
     * @param model
     * @return day page
     */
    @RequestMapping(value = "/day/{id}", method = RequestMethod.GET)
    public String DayPage(@PathVariable("id") int id, Model model){
        User principal = this.getPrincipalUser();
        model.addAttribute("day", dayService.getDayById(id));

        Shift neededShift = this.userDayService.getNeededShift(this.dayService.getDayById(id), true);
        if(this.dayService.notAuthorized(id)){return "errors/403";}

        for(Role role: principal.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){
                model.addAttribute("userDays", userDayService.listUserDays(id));
                model.addAttribute("shift", neededShift);
                return "owner-day-manager";
            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){
                model.addAttribute("member", true);

                List<UserDay> userDays = this.userDayService.listUserDays(id);
                for(UserDay userDay: userDays){
                    if(userDay.getUser().getId() == principal.getId()){
                        if(userDay.getShift() != neededShift){
                            model.addAttribute("shift", neededShift);
                        }else{
                            model.addAttribute("shift", "NONE");
                        }
                    }
                }

                model.addAttribute("userDay", userDayService.findUserDay(principal.getId(), id));
                return "member-day-manager";
            }
        }
        return "login";
    }

    /**
     * Render the configuration page of an specific period
     * @param id
     * @param model
     * @return calendar-configure page
     */
    @RequestMapping(value = "/calendar/configure/{id}", method = RequestMethod.GET)
    public String CalendarConfigurePage(@PathVariable("id") int id, Model model){
        if(this.periodService.notAuthorized(id)){return "errors/403";}
        model.addAttribute("days", this.dayService.listDays(id));
        return "calendar-configure";
    }

    /**
     * Render the notification page
     * @param model
     * @return notification page
     */
    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public  String NotificationsPage(Model model){
        int team_id  = this.getPrincipalUser().getTeam().getId();
        model.addAttribute("_notifications", this.notificationService.listNotifications(team_id));
        return "notification";
    }


    /**
     * Render to 403 page
     * @return not authorized page
     */
    @RequestMapping("/403")
    public String notAllowed(){
        return "errors/403";
    }
}
