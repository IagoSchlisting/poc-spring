package com.sap.controllers;
import com.sap.models.Day;
import com.sap.models.User;
import com.sap.models.UserDay;
import com.sap.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;


@Controller
public class CommonController {

    @Resource
    protected UserService userService;
    @Resource
    protected UserDayService userDayService;
    @Resource
    protected DayService dayService;
    @Resource
    protected TeamService teamService;
    @Resource
    protected PeriodService periodService;
    @Resource
    protected NotificationService notificationService;
    @Resource
    protected UserNotificationService userNotificationService;


    /**
     * Get current session user
     * @return object User
     */
    protected User getPrincipalUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByName(authentication.getName());
    }

    /**
     * Save user / day bond in the database (used by user and calendar controllers)
     * @param user
     * @param day
     */
    protected void createUserDay(User user, Day day){
        UserDay userDay = new UserDay();
        userDay.setDay(day);
        userDay.setUser(user);
        userDay.setShift(this.userDayService.getNeededShift(day, false));
        userDay.setAnyShift(true);
        userDay.setDisponibility(true);
        this.userDayService.addUserDay(userDay);
    }
}
