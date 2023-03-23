package com.ecsite.springbootoracle.controller.admin;

import com.ecsite.springbootoracle.dto.AdminDto;
import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;


@Controller
//@RequestMapping("admin")
public class UserController {

    @Autowired
    private AdminService userService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender mailSender;


    @GetMapping("activateUser/{id}")
    public String activateUser(@PathVariable("id") Long id, Model model) {
        Optional<Admin> user = adminRepository.findById(id);
        AdminDto adminDto = new AdminDto();
        if (user.isPresent()) {
            Admin admin = user.get();
            BeanUtils.copyProperties(admin, adminDto);
            adminDto.setActive(!adminDto.isActive());
            adminDto.setAccountRole("user");
            Admin updatedAdmin = userService.update(adminDto);
            if (updatedAdmin == null) {
                model.addAttribute("errorMessage", "User not found");
                return "redirect:/admin/userlist";
            }
            sendActivationEmail(adminDto);

            model.addAttribute("successMessage", "User status updated");
            return "redirect:/admin/userlist";
        }
        model.addAttribute("errorMessage", "User not found");
        return "redirect:/admin/userlist";
    }

    private void sendActivationEmail(AdminDto user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Account Status Changed");

        if (user.isActive()) {
            message.setText("Hello " + user.getFirstName() + ",\n\nYour account has been activated.\n\nThank you!");
        } else {
            message.setText("Hello " + user.getFirstName() + ",\n\nYour account has been deactivated.\n\nThank you!");
        }

        mailSender.send(message);
    }
}
