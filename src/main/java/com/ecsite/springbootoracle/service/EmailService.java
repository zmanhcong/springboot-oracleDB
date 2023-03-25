package com.ecsite.springbootoracle.service;

import com.ecsite.springbootoracle.model.Admin;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Async
    public void sendActivationEmail(Admin user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Account Status Changed");

        if (user.isActive()){
            message.setText("Hello " + user.getFirstName() + ",\n\nYour account has been actived.\n\nThank you!");
        }else {
            message.setText("Hello " + user.getFirstName()+ ",\n\nYour account has been deactivated.\n\nThank you!");
        }

        mailSender.send(message);
    }
}
