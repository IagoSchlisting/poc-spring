package com.sap.controllers;

import com.sap.models.*;
import com.sap.service.DayService;
import com.sap.service.PeriodService;
import com.sap.service.UserDayService;
import com.sap.service.UserService;
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
public class CalendarController {

    @Resource
    private PeriodService periodService;
    @Resource
    private UserService userService;
    @Resource
    private DayService dayService;
    @Resource
    private UserDayService userDayService;

    @Resource
    private User principal;

    @RequestMapping(value = "/calendar/admin", method = RequestMethod.GET)
    public String CalendarPage(Model model, WebRequest request){
        //User principal = userService.getUserByName(request.getUserPrincipal().getName());
        this.principal = userService.getUserByName(request.getUserPrincipal().getName());
        model.addAttribute("periods", periodService.listPeriods(principal.getTeam().getId()));
        return "calendar-admin";
    }

    @RequestMapping(value = "/calendar/manage/{id}", method = RequestMethod.GET)
    public String PeriodPage(@PathVariable("id") int id, Model model, WebRequest request){
        User user = userService.getUserByName(request.getUserPrincipal().getName());
        if(user.getRoles().get(1).getRole().equals("ROLE_MEMBER")){
            model.addAttribute("member", true);
        }
        model.addAttribute("days", dayService.listDays(id));
        return "period-days";
    }
    @RequestMapping(value = "/day/{id}", method = RequestMethod.GET)
    public String DayPage(@PathVariable("id") int id, Model model, WebRequest request){

        User user = userService.getUserByName(request.getUserPrincipal().getName());
        model.addAttribute("day", dayService.getDayById(id));

        for(Role role: user.getRoles()){
            if(new String(role.getRole()).equals("ROLE_OWNER")){
                model.addAttribute("userDays", userDayService.listUserDays(id));
                return "owner-day-manager";
            }else if(new String(role.getRole()).equals("ROLE_MEMBER")){
                model.addAttribute("member", true);
                model.addAttribute("userDay", userDayService.findUserDay(user.getId(), id));
                return "member-day-manager";
            }
        }
        return "login";
    }

    @RequestMapping(value = "/userDay/update", method = RequestMethod.POST)
    public String updateUserDay(Model model, WebRequest request){
        User user = userService.getUserByName(request.getUserPrincipal().getName());

        int id = Integer.parseInt(request.getParameter("id"));

        UserDay userDay = userDayService.getUserDayById(id);
        String shift = request.getParameter("shift");

        if(shift.equals("day")){
            userDay.setShift(Shift.DAY);
        }else if (shift.equals("late")){
            userDay.setShift(Shift.LATE);
        }else {
            userDay.setShift(Shift.ANY);
        }

        if(request.getParameterMap().containsKey("disponibility")){
            userDay.setDisponibility(request.getParameter("disponibility").equals("1") ? true : false);
        }

        model.addAttribute("member", true);

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

    @RequestMapping(value = "/day/admin/update", method = RequestMethod.POST)
    public String updateDay(WebRequest request, Model model){
        int id = Integer.parseInt(request.getParameter("id"));

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

    @RequestMapping(value = "/period/add", method = RequestMethod.POST)
    public String addNewPeriod(WebRequest request, Model model){

        // Get principal user
        //User principal = userService.getUserByName(request.getUserPrincipal().getName());
        int team_id = this.principal.getTeam().getId();

        List<Period> periods = periodService.listPeriods(team_id);
        // Try to format data
        LocalDate start = LocalDate.parse(request.getParameter("start-date"));
        LocalDate end = LocalDate.parse(request.getParameter("end-date"));
        // Validating data values
        if(start.isBefore(LocalDate.now()) || start.isAfter(end)){
            model.addAttribute("error", "Not possible to create a period between this dates, check the corresponding values!");
            model.addAttribute("periods", periods);
            return "calendar-admin";
        }
        //Checking if period data is already been used.
        if(!validatePeriods(periods, start, end)){
            model.addAttribute("error", "Not possible to create the period, some days between the chosen interval has already been used!");
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

    public void boundUsersToTheDate(Day day){
        List<User> users = this.userService.listUsers(this.principal.getTeam().getId(), this.principal.getId());
        UserDay userDay;
        for(User user : users){
            userDay = new UserDay();
            userDay.setDay(day);
            userDay.setUser(user);
            userDay.setDisponibility(true);
            userDay.setShift(Shift.ANY);
            userDayService.addUserDay(userDay);
        }
    }


    public Boolean validatePeriods(List<Period> periods, LocalDate start, LocalDate end){
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

    @RequestMapping("/calendar/delete/{id}")
    public String removePeriod(@PathVariable("id") int id, Model model, WebRequest request){
        //User principal = userService.getUserByName(request.getUserPrincipal().getName());
        periodService.removePeriod(id);
        model.addAttribute("periods", periodService.listPeriods(this.principal.getTeam().getId()));
        model.addAttribute("msg", "Period removed successfully!");
        return "calendar-admin";
    }
}
