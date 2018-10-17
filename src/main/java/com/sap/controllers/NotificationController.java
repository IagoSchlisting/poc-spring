package com.sap.controllers;

import com.sap.models.Notification;
import com.sap.models.User;
import com.sap.models.UserNotification;
import com.sap.service.NotificationService;
import com.sap.service.UserNotificationService;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationController extends CommonController{

    @Resource
    private NotificationService notificationService;
    @Resource
    private UserNotificationService userNotificationService;

    @RequestMapping(value = "/notification/add", method = RequestMethod.POST)
    public RedirectView addNotification(RedirectAttributes redirectAttributes, Notification notification){
        try{
            notification.setTeam(this.getPrincipalUser().getTeam());
            this.notificationService.addNotification(notification);
            this.boundNotificationToUser(notification);
            redirectAttributes.addFlashAttribute("msg", "Notification sent with success!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/notification");
    }

    @RequestMapping(value = "/notification/delete/{id}", method = RequestMethod.GET)
    public RedirectView removeNotification(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        try {
            Notification notification = this.notificationService.getNotificationById(id);
            this.notificationService.removeNotification(notification);
            redirectAttributes.addFlashAttribute("msg", "Notification deleted with success!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/notification");
    }

    private void boundNotificationToUser(Notification notification){
        List<User> users = this.userService.listUsers(this.getPrincipalUser().getTeam().getId());
        UserNotification userNotification;

        for(User user : users){
            userNotification = new UserNotification();
            userNotification.setNotification(notification);
            userNotification.setUser(user);
            userNotification.setVisualized(false);
            this.userNotificationService.addUserNotification(userNotification);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/visualize/notification", method = RequestMethod.POST)
    public void setNotificationAsVisualized(@RequestBody Notification notification){
        int notification_id = notification.getId();
        UserNotification userNotification = this.userNotificationService.getUserNotification(notification_id, this.getPrincipalUser().getId());
        userNotification.setVisualized(true);
        this.userNotificationService.updateUserNotification(userNotification);
    }

}
