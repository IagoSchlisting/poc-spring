package com.sap.controllers;

import com.sap.dto.DayDTO;
import com.sap.dto.PeriodDTO;
import com.sap.dto.UserDayDTO;
import com.sap.models.*;
import com.sap.service.DayService;
import com.sap.service.PeriodService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;


@Controller
public class CalendarController extends CommonController {

    @Resource
    private PeriodService periodService;
    @Resource
    private DayService dayService;

    /**
     * Allows owner to create a new period if the same respects the validations
     * @param redirectAttributes
     * @return updated calendar-admin page
     */
    @RequestMapping(value = "/period/add", method = RequestMethod.POST)
    public RedirectView addNewPeriod(RedirectAttributes redirectAttributes, PeriodDTO period){
        try{
            period.setTeam(this.getPrincipalUser().getTeam());
            Period new_period = periodService.addPeriod(period);
            createDaysFromPeriod(new_period);
            redirectAttributes.addFlashAttribute("msg", "Period added successfully!");
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/calendar/admin");
    }


    /**
     * Allows member user to update his disponibility and shift informations from specifc day
     * @param redirectAttributes
     * @return member-day-manager page
     */
    @RequestMapping(value = "/userDay/update", method = RequestMethod.POST)
    public RedirectView updateUserDay(RedirectAttributes redirectAttributes, UserDayDTO userDay){
        redirectAttributes.addFlashAttribute("member", true);
        try{
            userDayService.updateUserDay(userDay);
            redirectAttributes.addFlashAttribute("msg", "Changes Saved!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new RedirectView("/");
        }
        return new RedirectView("/day/" + this.userDayService.getUserDayById(userDay.getId()).getDay().getId());
    }

    /**
     * Allows owners to set day as weekend or holiday
     * @param redirectAttributes
     * @return owner-day-manager page
     */
    @RequestMapping(value = "/day/admin/update", method = RequestMethod.POST)
    public RedirectView updateDay(RedirectAttributes redirectAttributes, DayDTO day){
        if(this.dayService.notAuthorized(day.getId())){return new RedirectView("/403");}
        try{
            dayService.updateDay(day);
            redirectAttributes.addFlashAttribute("msg", "Changed!");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/day/"+day.getId());
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
     * Method responsible for deleting the period. It automatically deletes all days and bounded users from the database.
     * @param id
     * @param redirectAttributes
     * @return calendar-admin page
     */
    @RequestMapping("/calendar/delete/{id}")
    public RedirectView removePeriod(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        if(this.periodService.notAuthorized(id)){return new RedirectView("/403");}
        periodService.removePeriod(id);
        redirectAttributes.addFlashAttribute("msg", "Period removed successfully!");
        return new RedirectView("/calendar/admin");
    }

}
