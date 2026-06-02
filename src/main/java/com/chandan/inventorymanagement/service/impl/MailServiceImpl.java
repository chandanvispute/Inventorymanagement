package com.chandan.inventorymanagement.service.impl;

import com.chandan.inventorymanagement.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final OtpHandler otpHandler;

    public MailServiceImpl(JavaMailSender mailSender, OtpHandler otpHandler) {
        this.mailSender = mailSender;
        this.otpHandler = otpHandler;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public String sendOtp(String to){
        String otp = generateOtp();
        sendEmail(to, "OTP BY Iventory Management", String.valueOf(otp));
        otpHandler.setOtp(to,otp);
        System.out.println("TO : "+to+"     |     OTP"+otp);
        return otp;
    }

    public String generateOtp() {
        return String.valueOf(((int)(Math.random() * 900000) + 100000));
    }
}
