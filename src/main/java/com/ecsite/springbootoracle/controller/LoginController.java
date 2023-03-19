package com.ecsite.springbootoracle.controller;

import com.ecsite.springbootoracle.dto.AdminDto;
import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.RoleRepository;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AdminService adminService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/admin-page")
    public String afterLoginRoleAdmin(){
        return "/admin/admin-page";
    }

    @GetMapping("/user-homepage")
    public String afterLoginRoleUser(){
        return "user/user-homepage";
    }

    @GetMapping("/register")
    public String register(Model model, AdminDto adminDto, HttpSession session){
        if (session.getAttribute("message") != null){
            session.removeAttribute("message");
        }
        if (session.getAttribute("successMessage") != null){
            session.removeAttribute("successMessage");
        }
        model.addAttribute("adminDto", adminDto);
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(Model model,
                             HttpSession session,
                             @Valid @ModelAttribute("adminDto")AdminDto adminDto,
                             BindingResult result){
        if (session.getAttribute("message") != null){
            session.removeAttribute("message");
        }
        if (session.getAttribute("successMessage") != null){
            session.removeAttribute("successMessage");
        }
        try{
            if (result.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                session.setAttribute("message", "Input wrong");
                return "register";
            }
            Admin findExisted = adminService.findByUsername(adminDto.getUsername());
            if (findExisted != null){
                session.setAttribute("message", "An account is already registered with your email address. Please login.");
                System.out.println("Already registed!");
                return "register";
            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                model.addAttribute("adminDto", adminDto);
                session.setAttribute("successMessage", "Register successfully");
                System.out.println("save successfully");
                return "register";
            }else{
                session.setAttribute("message", "The Repeat Password dost not match.");
                return "register";
            }
        }catch (Exception e){

        }
        return "register";
    }
}
