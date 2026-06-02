package com.chandan.inventorymanagement.service.impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OtpHandler {

    private HashMap<String,String> otpStore;

    public OtpHandler(){
        otpStore=new HashMap<>();
    }

    public String getOtp(String email){
        return otpStore.get(email);
    }

    public void setOtp(String email, String otp) {
        otpStore.put(email,otp);
    }
}
