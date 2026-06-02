package com.chandan.inventorymanagement.controller;

import com.chandan.inventorymanagement.config.PasswordConfig;
import com.chandan.inventorymanagement.dto.AuthRequest;
import com.chandan.inventorymanagement.dto.RegisterRequest;
import com.chandan.inventorymanagement.entity.Role;
import com.chandan.inventorymanagement.entity.User;
import com.chandan.inventorymanagement.exception.ResourceNotFoundException;
import com.chandan.inventorymanagement.repository.UserRepository;
import com.chandan.inventorymanagement.security.jwt.JwtUtil;
import com.chandan.inventorymanagement.service.impl.OtpHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final OtpHandler otpHandler;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          UserDetailsService userDetailsService,
                          OtpHandler otpHandler) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.otpHandler = otpHandler;
    }

    // LOGIN (already present)
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        System.out.println("ionasc");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        try {
            return jwtUtil.generateToken(userDetailsService.loadUserByUsername(request.getUsername()));
        } catch (Exception e) {
            e.printStackTrace();   // IMPORTANT
            throw e;
        }
    }

    // REGISTER (NEW)
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        if(!request.getOtp().equals(otpHandler.getOtp(request.getUsername()))){
            throw new RuntimeException("Invalid OTP");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
