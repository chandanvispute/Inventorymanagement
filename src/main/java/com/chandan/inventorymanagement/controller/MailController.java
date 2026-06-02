package com.chandan.inventorymanagement.controller;

import com.chandan.inventorymanagement.dto.SendMail;
import com.chandan.inventorymanagement.service.MailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mail")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService){
        this.mailService = mailService;
    }

    @GetMapping("/send")
    public void sendEmail(@RequestBody SendMail mail){
        mailService.sendEmail(mail.to,mail.subject, mail.body);
    }

    @GetMapping("/sendOtp")
    public String sendOtp(@RequestParam String to){
        return mailService.sendOtp(to);
    }
}
