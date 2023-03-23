package com.ecsite.springbootoracle.controller.admin;
import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            admin.setActive(!admin.isActive()); //change status of user.
            adminRepository.save(admin);
            sendActivationEmail(admin);

            model.addAttribute("successMessage", "User status updated");
            return "redirect:/admin/user/list";
        }
        model.addAttribute("errorMessage", "User not found");
        return "redirect:/admin/user/list";
    }

    private void sendActivationEmail(Admin user) {
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
