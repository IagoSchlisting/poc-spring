package com.sap.controllers;

import com.sap.models.*;
import com.sap.service.DayService;
import com.sap.service.PeriodService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;


@Controller
public class CalendarController extends BaseController{

    @Resource
    private PeriodService periodService;
    @Resource
    private DayService dayService;


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
        if(this.notAuthorized(id, "period")){return "errors/403";}
        if(this.getPrincipalUser().getRoles().get(1).getRole().equals("ROLE_MEMBER")){
            model.addAttribute("member", true);
        }
        model.addAttribute("days", dayService.listDays(id));
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

        if(this.notAuthorized(id, "day")){return "errors/403";}

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

    /**
     * Allows member user to update his disponibility and shift informations from specifc day
     * @param model
     * @param request
     * @return member-day-manager page
     */
    @RequestMapping(value = "/userDay/update", method = RequestMethod.POST)
    public String updateUserDay(Model model, WebRequest request){
        model.addAttribute("member", true);

        int id = Integer.parseInt(request.getParameter("id"));
        UserDay userDay = userDayService.getUserDayById(id);
        String shift = request.getParameter("shift");

        switch (shift){
            case "day":
                userDay.setShift(Shift.DAY);
                break;
            case "late":
                userDay.setShift(Shift.LATE);
                break;
            default:
                userDay.setShift(Shift.ANY);
                break;
        }

        if(request.getParameterMap().containsKey("disponibility")){
            userDay.setDisponibility(request.getParameter("disponibility").equals("1") ? true : false);
        }

        try{
            userDayService.updateUserDay(userDay);
            model.addAttribute("userDay", userDayService.getUserDayById(id));
            model.addAttribute("day", userDay.getDay());
            model.addAttribute("msg", "Changes Saved!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "member-day-manager";
    }

    /**
     * Allows owners to set day as weekend or holiday
     * @param request
     * @param model
     * @return owner-day-manager page
     */
    @RequestMapping(value = "/day/admin/update", method = RequestMethod.POST)
    public String updateDay(WebRequest request, Model model){
        int id = Integer.parseInt(request.getParameter("id"));
        if(this.notAuthorized(id, "day")){return "errors/403";}

        try{
            Day day = dayService.getDayById(id);
            day.setSpecial(request.getParameter("special").equals("1") ? true : false);
            dayService.updateDay(day);
            model.addAttribute("msg", "Changed!");
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());

        }

        model.addAttribute("day", dayService.getDayById(id));
        model.addAttribute("userDays", userDayService.listUserDays(id));
        return "owner-day-manager";
    }

    /**
     * Allows owner to create a new period if the same respects the validations
     * @param request
     * @param model
     * @return updated calendar-admin page
     */
    @RequestMapping(value = "/period/add", method = RequestMethod.POST)
    public String addNewPeriod(WebRequest request, Model model){

        User principal = this.getPrincipalUser();
        int team_id = principal.getTeam().getId();

        List<Period> periods = periodService.listPeriods(team_id);
        LocalDate start = LocalDate.parse(request.getParameter("start-date"));
        LocalDate end = LocalDate.parse(request.getParameter("end-date"));

        if(!validatePeriods(periods, start, end)){
            model.addAttribute("error", "Not possible to create a period between this dates, check the corresponding values!");
            model.addAttribute("periods", periods);
            return "calendar-admin";
        }

        try{
            Period period = new Period();
            period.setStart(start);
            period.setEnd(end);
            period.setTeam(principal.getTeam());
            periodService.addPeriod(period);
            createDaysFromPeriod(period);
            model.addAttribute("periods", periodService.listPeriods(team_id));
            model.addAttribute("msg", "Period added successfully!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }

        return "calendar-admin";
    }

    /**
     * Create individual days to the last added period
     * @param period
     */
    public void createDaysFromPeriod(Period period){
        LocalDate counter = period.getStart();
        LocalDate end = period.getEnd().plusDays(1);
        Day day;
        do{
            day = new Day();
            day.setPeriod(period);
            day.setDay(counter);
            day.setSpecial(false);
            dayService.addDay(day);
            boundUsersToTheDate(day);
            counter = counter.plusDays(1);
        }while(!counter.isEqual(end));
    }

    /**
     * Bound each of the owner's users to the day from the new created period
     * @param day
     */
    public void boundUsersToTheDate(Day day){
        List<User> users = this.userService.listUsers(this.getPrincipalUser().getTeam().getId(), this.getPrincipalUser().getId());
        for(User user : users){
            this.createUserDay(user, day);
        }
    }

    /**
     * Validate if there are no conflicts between each of the period's days
     * @param periods
     * @param start
     * @param end
     * @return Boolean which symbolizes success or error
     */
    public Boolean validatePeriods(List<Period> periods, LocalDate start, LocalDate end){
        if(start.isBefore(LocalDate.now()) || start.isAfter(end)){return false;}
        for(Period period : periods){
            if (start.isEqual(period.getStart())
                    || start.isEqual(period.getEnd())
                    || end.isEqual(period.getEnd())
                    || end.isEqual(period.getStart())
                    || (start.isAfter(period.getStart()) && start.isBefore(period.getEnd()))
                    || (start.isBefore(period.getStart()) && end.isAfter(period.getStart()))
            ){
                return false;
            }
        }
        return true;
    }

    /**
     * Method responsible for deleting the period. It automatically deletes all days and bounded users from the database.
     * @param id
     * @param model
     * @return calendar-admin page
     */
    @RequestMapping("/calendar/delete/{id}")
    public String removePeriod(@PathVariable("id") int id, Model model){
        if(this.notAuthorized(id, "period")){return "errors/403";}

        User principal = this.getPrincipalUser();
        periodService.removePeriod(id);
        model.addAttribute("periods", periodService.listPeriods(principal.getTeam().getId()));
        model.addAttribute("msg", "Period removed successfully!");
        return "calendar-admin";
    }

    public Boolean notAuthorized(int id, String type) {
        User principal = this.getPrincipalUser();

        if(type.equals("day")){
            Day day = this.dayService.getDayById(id);
            if (principal.getTeam().getId() != day.getPeriod().getTeam().getId()) {
                return true;
            }
        }

        if(type.equals("period")){
            Period period = this.periodService.getPeriodById(id);
            if (principal.getTeam().getId() != period.getTeam().getId()) {
                return true;
            }
        }

        return false;
    }
}
