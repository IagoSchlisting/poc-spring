package com.sap.controllers;

import com.sap.models.Notification;
import com.sap.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;

@Controller
public class NotificationController extends CommonController{

    @Resource
    private NotificationService notificationService;

    @RequestMapping(value = "/notification/add", method = RequestMethod.POST)
    public RedirectView addNotification(RedirectAttributes redirectAttributes, Notification notification){
        try{
            notification.setTeam(this.getPrincipalUser().getTeam());
            this.notificationService.addNotification(notification);
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

}
