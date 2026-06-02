package com.chandan.inventorymanagement.service.impl;

import com.chandan.inventorymanagement.entity.Role;
import com.chandan.inventorymanagement.entity.User;
import com.chandan.inventorymanagement.repository.UserRepository;
import com.chandan.inventorymanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.chandan.inventorymanagement.entity.Role.ROLE_ADMIN;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(ROLE_ADMIN);
        return userRepository.save(user);
    }

    @Override
    public String deleteUser(Long userId){
        userRepository.deleteById(userId);
        return "Successfully Delete User";
    }
}
