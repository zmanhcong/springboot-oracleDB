package com.ecsite.springbootoracle.controller;

import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AdminService adminService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/admin-page")
    public String afterLoginRoleAdmin(){
        return "/admin/admin-page";
    }

    @GetMapping("/shop")
    public String afterLoginRoleUser(){
        return "shop";
    }
}
