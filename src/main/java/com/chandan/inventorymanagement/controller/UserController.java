package com.chandan.inventorymanagement.controller;

import com.chandan.inventorymanagement.entity.User;
import com.chandan.inventorymanagement.repository.UserRepository;
import com.chandan.inventorymanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.chandan.inventorymanagement.entity.Role.ROLE_ADMIN;
import static com.chandan.inventorymanagement.entity.Role.ROLE_USER;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public List<User> getAllAdminUsers(){
        return userRepository.findByRole(ROLE_ADMIN);
    }

    @GetMapping("/user")
    public List<User> getAllUserUsers(){
        return userRepository.findByRole(ROLE_USER);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @DeleteMapping
    public String deleteUser(@Valid @RequestBody Long userId){
        return userService.deleteUser(userId);
    }
}
