package com.sap.controllers;

import com.sap.models.User;
import com.sap.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class BaseController {

    @Resource
    protected UserService userService;

    public User getPrincipalUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByName(authentication.getName());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
