package com.chandan.inventorymanagement.security.oauth;

import com.chandan.inventorymanagement.entity.Role;
import com.chandan.inventorymanagement.entity.User;
import com.chandan.inventorymanagement.repository.UserRepository;
import com.chandan.inventorymanagement.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuthSuccessHandler
        implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public OAuthSuccessHandler(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            UserDetailsService userDetailsService) {

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser.getAttribute("email");

        User user =
                userRepository.findByUsername(email)
                        .orElseGet(() -> {

                            User u = new User();

                            u.setUsername(email);

                            u.setRole(Role.ROLE_USER);

                            return userRepository.save(u);
                        });

        String jwt =
                jwtUtil.generateToken(userDetailsService.loadUserByUsername(email));

        String redirectUrl = "http://localhost:5173/oauth2/redirect?token=" +
                URLEncoder.encode(jwt, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}