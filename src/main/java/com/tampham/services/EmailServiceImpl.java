package com.tampham.services;

import com.tampham.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String from;

    @Override
    public boolean sendVerifyCode(User user) {
        if (user != null){
            try {
                SimpleMailMessage message = new SimpleMailMessage();

                message.setFrom(from);
                message.setTo(user.getEmail());
                message.setText(user.getVerifyCode());
                message.setSubject("Xác thực tài khoản");

                // Sending the mail
                javaMailSender.send(message);
                return true;
            } catch (Exception e){
                return false;
            }
        }
        return false;
    }
}
