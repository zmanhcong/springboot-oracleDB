package com.ecsite.springbootoracle.controller.admin;
import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.service.AdminService;
import com.ecsite.springbootoracle.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
//@RequestMapping("admin")
public class UserController {

    @Autowired
    private AdminService userService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("activateUser/{id}")
    public String activateUser(@PathVariable("id") Long id, Model model) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            admin.setActive(!admin.isActive()); //change status of user.
            adminRepository.save(admin);
            emailService.sendActivationEmail(admin);

            model.addAttribute("successMessage", "User status updated");
            return "redirect:/admin/user/list";
        }
        model.addAttribute("errorMessage", "User not found");
        return "redirect:/admin/user/list";
    }

    //Add csrfToken for avoid forbidden by springsecurity
    @RequestMapping("/admin/user/list")
    public String userList(Model model, HttpServletRequest request) {
        // Add this line to include CSRF token in the model
        model.addAttribute("_csrf", (CsrfToken) request.getAttribute(CsrfToken.class.getName()));
        return "/admin/userlist";
    }
}
