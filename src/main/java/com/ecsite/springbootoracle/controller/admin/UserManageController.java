package com.ecsite.springbootoracle.controller.admin;

import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class UserManageController {
    @Autowired
    AdminService adminService;

    @Autowired
    AdminRepository adminRepository;

    @GetMapping("/user/list")
    public String userList(Model model){
        List<Admin> userlist = adminRepository.findAll();
        if (userlist == null){
            model.addAttribute("message", "User is not found!!");
        }else {
            model.addAttribute("userlist", userlist);
        }
        return "/admin/userlist";
    }

}
