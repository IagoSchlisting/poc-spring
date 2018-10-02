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
        return "calendar-manage";
    }

    @RequestMapping(value = "/period/add", method = RequestMethod.POST)
    public String addNewPeriod(WebRequest request, Model model){

        // Try to format data
        String start = request.getParameter("start-date");
        String end = request.getParameter("end-date");

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
