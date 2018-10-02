package com.sap.controllers;

import com.sap.models.Period;
import com.sap.models.User;
import com.sap.service.PeriodService;
import com.sap.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class CalendarController {

    @Resource
    private PeriodService periodService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public String goToCalendarPage(Model model){
        model.addAttribute("periods", periodService.listPeriods());
        return "calendar-admin";
    }

    @RequestMapping(value = "/calendar/manage/{id}", method = RequestMethod.GET)
    public String manageCalendarPage(@PathVariable("id") int id){
        return "period-days";
    }

    @RequestMapping(value = "/period/add", method = RequestMethod.POST)
    public String addNewPeriod(WebRequest request, Model model){

        List<Period> periods = periodService.listPeriods();
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
        for(Period period : periods){
            if (start.isEqual(period.getStart())
            || start.isEqual(period.getEnd())
            || end.isEqual(period.getEnd())
            || end.isEqual(period.getStart())
            || (start.isAfter(period.getStart()) && end.isBefore(period.getEnd()))
            || (start.isAfter(period.getStart()) && start.isBefore(period.getEnd()))
            ){
                model.addAttribute("error", "Not possible to create the period, some days between the chosen interval has already been used!");
                model.addAttribute("periods", periods);
                return "calendar-admin";
            }
        }
        // Get principal user
        User principal = userService.getUserByName(request.getUserPrincipal().getName());
        try{
            Period period = new Period();
            period.setStart(start);
            period.setEnd(end);
            period.setTeam(principal.getTeam());
            periodService.addPeriod(period);

            model.addAttribute("periods", periodService.listPeriods());
            model.addAttribute("msg", "Period added successfully!");
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }

        return "calendar-admin";
    }


    @RequestMapping("/calendar/delete/{id}")
    public String removePeriod(@PathVariable("id") int id, Model model){
        periodService.removePeriod(id);
        model.addAttribute("periods", periodService.listPeriods());
        model.addAttribute("msg", "Period removed successfully!");
        return "calendar-admin";
    }
}
