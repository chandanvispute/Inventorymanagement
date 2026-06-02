package com.chandan.inventorymanagement.service;

public interface MailService {
    void sendEmail(String to, String subject, String body);
    String sendOtp(String to);
}
