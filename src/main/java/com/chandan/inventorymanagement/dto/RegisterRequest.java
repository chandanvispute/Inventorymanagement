package com.chandan.inventorymanagement.dto;

import com.chandan.inventorymanagement.entity.Role;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private Role role;
    @NotBlank
    private String password;

    @NotBlank
    private String otp;

    public RegisterRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role=role;
    }

    public String getOtp() { return otp; }

    public void setOtp(String otp) { this.otp=otp; }
}
