package com.sap.controllers;

import com.sap.dto.UserDTO;
import com.sap.models.Day;
import com.sap.models.Shift;
import com.sap.models.User;
import com.sap.models.UserDay;
import com.sap.service.UserDayService;
import com.sap.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.validation.constraints.Null;
import java.util.List;

@Controller
public class CommonController {

    @Resource
    protected UserService userService;
    @Resource
    protected UserDayService userDayService;

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

        int requireDay = day.getNumberDay();
        int requireLate = day.getNumberLate();
        int totalDay = 0;
        int totalLate = 0;

        List<UserDay> memberDays = this.userDayService.listUserDays(day.getId());
        for (UserDay memberDay : memberDays) {
            if (memberDay.getShift() == Shift.DAY) {
                totalDay++;
            } else {
                totalLate++;
            }
        }

        // Verifies which shift needs more members
        if (requireDay - totalDay > requireLate - totalLate) {
            userDay.setShift(Shift.DAY);
        } else if (requireDay - totalDay < requireLate - totalLate) {
            userDay.setShift(Shift.LATE);
        } else if (requireDay - totalDay == 0 && requireLate - totalLate == 0) {
            throw new IllegalArgumentException("Not possible to bound user to the day, number required already fulfilled.");
        } else {
            userDay.setShift(Shift.DAY);
        }

        //userDay.setShift(Shift.ANY);
        userDay.setAnyShift(true);
        userDay.setDisponibility(true);
        this.userDayService.addUserDay(userDay);
    }
}
